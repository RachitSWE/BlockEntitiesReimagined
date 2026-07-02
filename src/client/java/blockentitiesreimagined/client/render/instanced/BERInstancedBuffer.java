package blockentitiesreimagined.client.render.instanced;

/* joml */
import org.joml.Matrix4f;

/* lwjgl */
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

/* java */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Manages the OpenGL VAO, VBO, and Instanced matrix buffers.
 */
public class BERInstancedBuffer {
    
    private int vaoId = 0;
    private int geometryVboId = 0;
    private int instanceVboId = 0;
    private int geometryVertexCount = 0;
    private int instanceCount;
    private final int maxInstances;
    private ByteBuffer instanceData;

    public BERInstancedBuffer(int maxInstances) {
        this.maxInstances = maxInstances;
        this.instanceData = ByteBuffer.allocateDirect(maxInstances * 64).order(ByteOrder.nativeOrder());
    }

    public void init() {
        if (vaoId != 0) return;

        vaoId = GL30.glGenVertexArrays();
        geometryVboId = GL15.glGenBuffers();
        instanceVboId = GL15.glGenBuffers();

        GL30.glBindVertexArray(vaoId);

        // Bind geometry VBO and enable attributes: position, color, uv, normal
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, geometryVboId);
        
        // Attrib 0: Position (3 floats)
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 48, 0);

        // Attrib 1: Color (4 floats)
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 48, 12);

        // Attrib 2: UV (2 floats)
        GL20.glEnableVertexAttribArray(2);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 48, 28);

        // Attrib 3: Normal (3 floats)
        GL20.glEnableVertexAttribArray(3);
        GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 48, 36);

        // Bind instance VBO and setup matrix attributes (Attrib 4, 5, 6, 7 with divisor 1)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
        for (int i = 0; i < 4; i++) {
            int attribIndex = 4 + i;
            GL20.glEnableVertexAttribArray(attribIndex);
            GL20.glVertexAttribPointer(attribIndex, 4, GL11.GL_FLOAT, false, 64, i * 16);
            GL33.glVertexAttribDivisor(attribIndex, 1);
        }

        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        if (vaoId != 0) {
            GL30.glBindVertexArray(vaoId);
        }
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void uploadGeometry(FloatBuffer buffer, int vertexCount) {
        this.geometryVertexCount = vertexCount;
        init();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, geometryVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void addInstance(Matrix4f transform) {
        if (this.instanceCount >= this.maxInstances) return;
        transform.get(this.instanceData);
        this.instanceData.position(this.instanceData.position() + 64);
        this.instanceCount++;
    }

    public void uploadInstances() {
        if (vaoId == 0 || instanceCount == 0) return;
        this.instanceData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instanceData, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public int getGeometryVertexCount() {
        return geometryVertexCount;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void reset() {
        this.instanceCount = 0;
        this.instanceData.clear();
    }

    public void destroy() {
        if (geometryVboId != 0) {
            GL15.glDeleteBuffers(geometryVboId);
            geometryVboId = 0;
        }
        if (instanceVboId != 0) {
            GL15.glDeleteBuffers(instanceVboId);
            instanceVboId = 0;
        }
        if (vaoId != 0) {
            GL30.glDeleteVertexArrays(vaoId);
            vaoId = 0;
        }
    }
}
