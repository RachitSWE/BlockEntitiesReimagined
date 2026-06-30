package blockentitiesreimagined.client.render.pool;

/* lwjgl */
import org.lwjgl.system.MemoryUtil;

/* java */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class BERBufferPool {
    private static final int CAPACITY = 1048576; // 1MB
    private static final int MAX_POOL_SIZE = Math.max(16, Runtime.getRuntime().availableProcessors() * 4);
    
    private static final ConcurrentLinkedQueue<ByteBuffer> POOL = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger POOL_SIZE = new AtomicInteger(0);

    static {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            POOL.offer(MemoryUtil.memAlloc(CAPACITY).order(ByteOrder.nativeOrder()));
            POOL_SIZE.incrementAndGet();
        }
    }

    private BERBufferPool() {}

    public static ByteBuffer acquire() {
        ByteBuffer buffer = POOL.poll();
        if (buffer != null) {
            POOL_SIZE.decrementAndGet();
            buffer.clear();
            return buffer;
        }
        // Fallback if pool is exhausted
        return MemoryUtil.memAlloc(CAPACITY).order(ByteOrder.nativeOrder());
    }

    public static void release(ByteBuffer buffer) {
        if (POOL_SIZE.get() < MAX_POOL_SIZE) {
            POOL.offer(buffer);
            POOL_SIZE.incrementAndGet();
        } else {
            MemoryUtil.memFree(buffer);
        }
    }

    public static void destroy() {
        ByteBuffer buffer;
        while ((buffer = POOL.poll()) != null) {
            MemoryUtil.memFree(buffer);
        }
        POOL_SIZE.set(0);
    }
}
