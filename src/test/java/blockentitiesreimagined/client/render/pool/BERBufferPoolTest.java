package blockentitiesreimagined.client.render.pool;

/* java */
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BERBufferPoolTest {
    @Test
    public void testAcquireAndRelease() {
        ByteBuffer b1 = BERBufferPool.acquire();
        Assertions.assertNotNull(b1);
        Assertions.assertEquals(0, b1.position());
        
        BERBufferPool.release(b1);
        ByteBuffer b2 = BERBufferPool.acquire();
        Assertions.assertNotNull(b2);
        BERBufferPool.release(b2);
    }

    @Test
    public void testCapacity() {
        ByteBuffer b1 = BERBufferPool.acquire();
        try {
            Assertions.assertEquals(1048576, b1.capacity(), "Buffer capacity should be 1MB");
        } finally {
            BERBufferPool.release(b1);
        }
    }
    
    @Test
    public void testExhaustion() {
        // Acquire 128 buffers to ensure we exhaust it
        List<ByteBuffer> buffers = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            buffers.add(BERBufferPool.acquire());
        }
        
        for (ByteBuffer b : buffers) {
            BERBufferPool.release(b);
        }
        
        // After releasing, acquire one to ensure it's still working
        ByteBuffer b = BERBufferPool.acquire();
        Assertions.assertNotNull(b);
        BERBufferPool.release(b);
    }
    
    @Test
    public void testConcurrency() throws InterruptedException {
        int threads = 32;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        ByteBuffer b = BERBufferPool.acquire();
                        Assertions.assertNotNull(b);
                        // Simulate some work
                        Thread.yield();
                        BERBufferPool.release(b);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
    }
}
