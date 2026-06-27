package blockentitiesreimagined.client.render.pool;

/* java */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class BERBufferPool {
    private static final int CAPACITY = 262144; // 256KB
    private static final int MAX_POOL_SIZE = Math.max(16, Runtime.getRuntime().availableProcessors() * 4);
    
    private static final ConcurrentLinkedQueue<ByteBuffer> POOL = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger CURRENT_SIZE = new AtomicInteger(0);

    static {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            POOL.offer(ByteBuffer.allocateDirect(CAPACITY).order(ByteOrder.nativeOrder()));
            CURRENT_SIZE.incrementAndGet();
        }
    }

    private BERBufferPool() {}

    public static ByteBuffer acquire() {
        ByteBuffer buffer = POOL.poll();
        if (buffer != null) {
            CURRENT_SIZE.decrementAndGet();
            buffer.clear();
            return buffer;
        }
        // Fallback if pool is exhausted
        return ByteBuffer.allocateDirect(CAPACITY).order(ByteOrder.nativeOrder());
    }

    public static void release(ByteBuffer buffer) {
        if (CURRENT_SIZE.get() < MAX_POOL_SIZE) {
            POOL.offer(buffer);
            CURRENT_SIZE.incrementAndGet();
        }
        // If the pool is already at capacity, we just let the buffer fall out of scope for GC.
    }
}
