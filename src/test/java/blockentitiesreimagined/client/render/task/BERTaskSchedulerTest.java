package blockentitiesreimagined.client.render.task;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/* jetbrains */
import org.jetbrains.annotations.Nullable;

/* java */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BERTaskSchedulerTest {

    private static class DummyRebuildTask implements BERTaskScheduler.IRebuildTask {
        private final AtomicInteger executionCount;
        private final AtomicInteger dirtyCount;
        @Nullable
        private final CountDownLatch latch;
        
        public DummyRebuildTask(AtomicInteger executionCount, AtomicInteger dirtyCount, @Nullable CountDownLatch latch) {
            this.executionCount = executionCount;
            this.dirtyCount = dirtyCount;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (latch != null) {
                try {
                    latch.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            executionCount.incrementAndGet();
        }

        @Override
        public void markDirty() {
            dirtyCount.incrementAndGet();
        }
    }

    @Test
    public void testTaskRejectionMarksDirty() throws InterruptedException {
        // We will overwhelm the task scheduler.
        // The queue capacity is 1000. We will submit 2000 tasks.
        AtomicInteger executionCount = new AtomicInteger(0);
        AtomicInteger dirtyCount = new AtomicInteger(0);
        
        // This latch will block the worker threads so the queue fills up.
        CountDownLatch workerBlocker = new CountDownLatch(1);
        
        for (int i = 0; i < 2000; i++) {
            BERTaskScheduler.submit(new DummyRebuildTask(executionCount, dirtyCount, workerBlocker));
        }
        
        // At this point, the queue is full. At least ~1000 tasks should have been rejected and marked dirty.
        // Wait a tiny bit for the executor to process rejections (though submit is mostly synchronous in executor).
        Thread.sleep(200);
        
        Assertions.assertTrue(dirtyCount.get() > 0, "Tasks should have been discarded and marked dirty");
        
        // Release the workers.
        workerBlocker.countDown();
        
        // Give time to finish executions.
        long startTime = System.currentTimeMillis();
        while (executionCount.get() + dirtyCount.get() < 2000 && (System.currentTimeMillis() - startTime) < 5000) {
            Thread.sleep(50);
        }
        
        Assertions.assertTrue(executionCount.get() > 0, "Some tasks should have executed successfully");
        
        int totalProcessed = executionCount.get() + dirtyCount.get();
        // Almost all 2000 should be accounted for (some might be in transition).
        Assertions.assertTrue(totalProcessed > 1900, "Total executed + marked dirty should account for most submitted tasks");
    }
}
