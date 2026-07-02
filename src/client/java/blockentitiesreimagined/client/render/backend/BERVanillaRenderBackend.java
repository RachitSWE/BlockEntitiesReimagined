package blockentitiesreimagined.client.render.backend;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;
import blockentitiesreimagined.client.render.instanced.BERInstancedBuffer;
import blockentitiesreimagined.client.render.instanced.BERStaticEntityInstancer;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.jetbrains.annotations.NotNull;

public class BERVanillaRenderBackend implements IRenderBackend {

    private int shaderProgram = 0;
    private int mvpUniformLoc = -1;
    private final float[] mvpArray = new float[16];

    @Override
    public void init() {
        // Initialization performed lazily on first render call to guarantee active GL context
    }

    private void compileShader() {
        if (shaderProgram != 0) return;
        
        String vert = "#version 330 core\n" +
                "layout (location = 0) in vec3 aPos;\n" +
                "layout (location = 1) in vec4 aColor;\n" +
                "layout (location = 2) in vec2 aTexCoord;\n" +
                "layout (location = 3) in vec3 aNormal;\n" +
                "layout (location = 4) in mat4 instanceMatrix;\n" +
                "out vec4 FragColor;\n" +
                "out vec2 TexCoord;\n" +
                "uniform mat4 mvp;\n" +
                "void main() {\n" +
                "    gl_Position = mvp * instanceMatrix * vec4(aPos, 1.0);\n" +
                "    FragColor = aColor;\n" +
                "    TexCoord = aTexCoord;\n" +
                "}\n";

        String frag = "#version 330 core\n" +
                "in vec4 FragColor;\n" +
                "in vec2 TexCoord;\n" +
                "out vec4 fragColor;\n" +
                "uniform sampler2D texture_diffuse;\n" +
                "void main() {\n" +
                "    fragColor = texture(texture_diffuse, TexCoord) * FragColor;\n" +
                "}\n";

        int vs = org.lwjgl.opengl.GL20.glCreateShader(org.lwjgl.opengl.GL20.GL_VERTEX_SHADER);
        org.lwjgl.opengl.GL20.glShaderSource(vs, vert);
        org.lwjgl.opengl.GL20.glCompileShader(vs);
        
        int fs = org.lwjgl.opengl.GL20.glCreateShader(org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER);
        org.lwjgl.opengl.GL20.glShaderSource(fs, frag);
        org.lwjgl.opengl.GL20.glCompileShader(fs);

        shaderProgram = org.lwjgl.opengl.GL20.glCreateProgram();
        org.lwjgl.opengl.GL20.glAttachShader(shaderProgram, vs);
        org.lwjgl.opengl.GL20.glAttachShader(shaderProgram, fs);
        org.lwjgl.opengl.GL20.glLinkProgram(shaderProgram);

        mvpUniformLoc = org.lwjgl.opengl.GL20.glGetUniformLocation(shaderProgram, "mvp");

        org.lwjgl.opengl.GL20.glDeleteShader(vs);
        org.lwjgl.opengl.GL20.glDeleteShader(fs);
    }

    @Override
    public void dispatchInstancedDraws(@NotNull org.joml.Matrix4fc mvp, float tickDelta) {
        BERStaticEntityInstancer instancer = blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer();
        if (instancer == null) return;

        // Check if there are any active instances to draw across any buffer
        boolean hasInstances = false;
        for (BERInstancedBuffer buffer : instancer.getBuffers().values()) {
            if (buffer.getInstanceCount() > 0 && buffer.getGeometryVertexCount() > 0) {
                hasInstances = true;
                break;
            }
        }
        if (!hasInstances) return;

        compileShader();

        org.lwjgl.opengl.GL20.glUseProgram(shaderProgram);

        // Upload combined MVP matrix with zero allocation
        mvp.get(mvpArray);
        org.lwjgl.opengl.GL20.glUniformMatrix4fv(mvpUniformLoc, false, mvpArray);

        // Draw each active buffer
        for (java.util.Map.Entry<net.minecraft.world.level.block.entity.BlockEntityType<?>, BERInstancedBuffer> entry : instancer.getBuffers().entrySet()) {
            BERInstancedBuffer buffer = entry.getValue();
            if (buffer.getInstanceCount() > 0 && buffer.getGeometryVertexCount() > 0) {
                buffer.bind();
                org.lwjgl.opengl.GL31.glDrawArraysInstanced(org.lwjgl.opengl.GL11.GL_TRIANGLES, 0, buffer.getGeometryVertexCount(), buffer.getInstanceCount());
                buffer.unbind();
            }
            buffer.reset();
        }

        org.lwjgl.opengl.GL20.glUseProgram(0);
    }

    @Override
    public void renderDynamicEntity(@NotNull BlockEntityRenderState renderState,
                                    @NotNull PoseStack matrices,
                                    @NotNull SubmitNodeCollector collector,
                                    @NotNull CameraRenderState cameraRenderState) {
        // Dynamic rendering routing fallback
    }

    @Override
    public void destroy() {
        if (shaderProgram != 0) {
            org.lwjgl.opengl.GL20.glDeleteProgram(shaderProgram);
            shaderProgram = 0;
        }
    }
}
