package blockentitiesreimagined.client.render.task;

/* local */
import blockentitiesreimagined.client.BER;

/* java */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* jetbrains */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BERTaskScheduler {
    // Thread pool sizing: leave some cores for the main thread and standard chunk builder.
    private static final int THREADS = Math.max(1, (Runtime.getRuntime().availableProcessors() / 2));
    private static final int QUEUE_CAPACITY = 1000;
    
    private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    
    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger(1);
    
    @NotNull
    private static Thread createWorkerThread(@NotNull Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("BER-Worker-" + THREAD_COUNTER.getAndIncrement());
        thread.setPriority(Thread.MIN_PRIORITY + 1);
        thread.setDaemon(true);
        return thread;
    }

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            THREADS, THREADS,
            0L, TimeUnit.MILLISECONDS,
            QUEUE,
            BERTaskScheduler::createWorkerThread,
            new RebuildFeedbackRejectionHandler()
    );

    private BERTaskScheduler() {}

    public static void submit(@NotNull Runnable task) {
        try {
            EXECUTOR.execute(task);
        } catch (Exception e) {
            BER.getLogger().error("Failed to submit task to execution queue!", e);
        }
    }

    public static int getPendingTasksCount() {
        return QUEUE.size();
    }
    
    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }

    /**
     * Interface for tasks that can be flagged as dirty if they are rejected from the queue.
     */
    public interface IRebuildTask extends Runnable {
        void markDirty();
    }

    private static class RebuildFeedbackRejectionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(@NotNull Runnable r, @NotNull ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                // If the queue is full, we drop the oldest task to prioritize newer ones.
                @Nullable Runnable discarded = executor.getQueue().poll();
                
                // If the discarded task is a rebuild task, we must flag its chunk section as dirty 
                // so it gets rebuilt later instead of remaining forever invisible/stale.
                if (discarded instanceof IRebuildTask) {
                    ((IRebuildTask) discarded).markDirty();
                } else if (discarded != null) {
                    BER.getLogger().warn("BER task queue overflowed, old task completely discarded: {}", discarded);
                }
                
                // Now that we made room (or at least tried), attempt to queue the new task again.
                // If it fails again, we just run the new task's markDirty fallback.
                if (!executor.getQueue().offer(r)) {
                    if (r instanceof IRebuildTask) {
                        ((IRebuildTask) r).markDirty();
                    } else {
                        BER.getLogger().warn("BER task queue overflowed, task completely discarded: {}", r);
                    }
                }
            } else {
                if (r instanceof IRebuildTask) {
                    ((IRebuildTask) r).markDirty();
                }
            }
        }
    }
}
