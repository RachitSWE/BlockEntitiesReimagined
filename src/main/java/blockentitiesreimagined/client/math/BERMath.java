package blockentitiesreimagined.client.math;

/* joml */
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Utility class providing zero-allocation, thread-safe, mutex-free ring buffers for math vectors and matrix stacks.
 * 
 * <p><strong>CRITICAL WARNING:</strong> Pooled math objects returned by this class are strictly ephemeral.
 * Do not cache or share these instances across threads. If a calculated vector/matrix needs to be stored or 
 * sent to another thread (e.g., in a concurrent queue), it must be copied into a standard, non-pooled instance.
 */
public final class BERMath {
    private static final int POOL_SIZE = 256;

    private static final ThreadLocal<VectorPool3f> POOL_VEC3 = ThreadLocal.withInitial(VectorPool3f::new);
    private static final ThreadLocal<VectorPool4f> POOL_VEC4 = ThreadLocal.withInitial(VectorPool4f::new);
    private static final ThreadLocal<MatrixPool4f> POOL_MAT4 = ThreadLocal.withInitial(MatrixPool4f::new);
    private static final ThreadLocal<QuatPool> POOL_QUAT = ThreadLocal.withInitial(QuatPool::new);

    private BERMath() {}

    public static Vector3f getVec3() {
        return POOL_VEC3.get().next().set(0f, 0f, 0f);
    }

    public static Vector3f getVec3(float x, float y, float z) {
        return POOL_VEC3.get().next().set(x, y, z);
    }

    public static Vector4f getVec4() {
        return POOL_VEC4.get().next().set(0f, 0f, 0f, 0f);
    }

    public static Vector4f getVec4(float x, float y, float z, float w) {
        return POOL_VEC4.get().next().set(x, y, z, w);
    }

    public static Matrix4f getMat4() {
        return POOL_MAT4.get().next().identity();
    }

    public static Quaternionf getQuat() {
        return POOL_QUAT.get().next().identity();
    }

    private static class VectorPool3f {
        private final Vector3f[] pool = new Vector3f[POOL_SIZE];
        private int index = 0;

        VectorPool3f() {
            for (int i = 0; i < POOL_SIZE; i++) pool[i] = new Vector3f();
        }

        Vector3f next() {
            Vector3f v = pool[index];
            index = (index + 1) % POOL_SIZE;
            return v;
        }
    }

    private static class VectorPool4f {
        private final Vector4f[] pool = new Vector4f[POOL_SIZE];
        private int index = 0;

        VectorPool4f() {
            for (int i = 0; i < POOL_SIZE; i++) pool[i] = new Vector4f();
        }

        Vector4f next() {
            Vector4f v = pool[index];
            index = (index + 1) % POOL_SIZE;
            return v;
        }
    }

    private static class MatrixPool4f {
        private final Matrix4f[] pool = new Matrix4f[POOL_SIZE];
        private int index = 0;

        MatrixPool4f() {
            for (int i = 0; i < POOL_SIZE; i++) pool[i] = new Matrix4f();
        }

        Matrix4f next() {
            Matrix4f m = pool[index];
            index = (index + 1) % POOL_SIZE;
            return m;
        }
    }

    private static class QuatPool {
        private final Quaternionf[] pool = new Quaternionf[POOL_SIZE];
        private int index = 0;

        QuatPool() {
            for (int i = 0; i < POOL_SIZE; i++) pool[i] = new Quaternionf();
        }

        Quaternionf next() {
            Quaternionf q = pool[index];
            index = (index + 1) % POOL_SIZE;
            return q;
        }
    }

    public static final class MatrixStack {
        private static final int MAX_DEPTH = 32;
        private final Matrix4f[] stack = new Matrix4f[MAX_DEPTH];
        private int pointer = 0;

        public MatrixStack() {
            for (int i = 0; i < MAX_DEPTH; i++) {
                stack[i] = new Matrix4f();
            }
            stack[0].identity();
        }

        public void push() {
            if (pointer >= MAX_DEPTH - 1) {
                throw new IllegalStateException("Matrix stack overflow!");
            }
            stack[pointer + 1].set(stack[pointer]);
            pointer++;
        }

        public void pop() {
            if (pointer <= 0) {
                throw new IllegalStateException("Matrix stack underflow!");
            }
            pointer--;
        }

        public Matrix4f peek() {
            return stack[pointer];
        }

        public void clear() {
            pointer = 0;
            stack[0].identity();
        }
    }

    private static final ThreadLocal<MatrixStack> LOCAL_STACK = ThreadLocal.withInitial(MatrixStack::new);

    public static MatrixStack getStack() {
        MatrixStack stack = LOCAL_STACK.get();
        stack.clear();
        return stack;
    }
}
