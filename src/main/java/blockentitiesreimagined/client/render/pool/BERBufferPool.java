package blockentitiesreimagined.client.render.pool;

/* java */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ArrayBlockingQueue;

public final class BERBufferPool {
    private static final int CAPACITY = 262144; // 256KB
    private static final int MAX_POOL_SIZE = Math.max(16, Runtime.getRuntime().availableProcessors() * 4);
    
    private static final ArrayBlockingQueue<ByteBuffer> POOL = new ArrayBlockingQueue<>(MAX_POOL_SIZE);

    static {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            POOL.offer(ByteBuffer.allocateDirect(CAPACITY).order(ByteOrder.nativeOrder()));
        }
    }

    private BERBufferPool() {}

    public static ByteBuffer acquire() {
        ByteBuffer buffer = POOL.poll();
        if (buffer != null) {
            buffer.clear();
            return buffer;
        }
        // Fallback if pool is exhausted
        return ByteBuffer.allocateDirect(CAPACITY).order(ByteOrder.nativeOrder());
    }

    public static void release(ByteBuffer buffer) {
        POOL.offer(buffer);
        // If the pool is already at capacity, offer returns false and the buffer falls out of scope for GC.
    }
}
