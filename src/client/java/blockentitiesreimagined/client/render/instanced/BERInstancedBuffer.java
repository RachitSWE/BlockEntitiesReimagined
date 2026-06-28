package blockentitiesreimagined.client.render.instanced;

/* joml */
import org.joml.Matrix4f;

/* java */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Manages the OpenGL VAO, VBO, and Instanced matrix buffers.
 */
public class BERInstancedBuffer {
    
    private int vaoId;
    private int geometryVboId;
    private int instanceVboId;
    private int instanceCount;
    private final int maxInstances;
    private ByteBuffer instanceData;

    public BERInstancedBuffer(int maxInstances) {
        this.maxInstances = maxInstances;
        this.instanceData = ByteBuffer.allocateDirect(maxInstances * 64).order(ByteOrder.nativeOrder());
    }

    public void init() {
        // Initialize OpenGL buffers (glGenVertexArrays, glGenBuffers, etc.)
    }

    public void bind() {
        // glBindVertexArray
    }

    public void unbind() {
        // glBindVertexArray(0)
    }

    public void addInstance(Matrix4f transform) {
        if (this.instanceCount >= this.maxInstances) return;
        transform.get(this.instanceData);
        this.instanceData.position(this.instanceData.position() + 64);
        this.instanceCount++;
    }

    public void uploadInstances() {
        this.instanceData.flip();
        // glBufferData mapping for instanced VBO
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void reset() {
        this.instanceCount = 0;
        this.instanceData.clear();
    }

    public void destroy() {
        // glDeleteBuffers, glDeleteVertexArrays
    }
}
