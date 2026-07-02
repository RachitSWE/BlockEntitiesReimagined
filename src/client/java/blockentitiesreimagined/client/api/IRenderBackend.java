package blockentitiesreimagined.client.api;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.jetbrains.annotations.NotNull;

/**
 * Abstracts the rendering backend (Vanilla vs Sodium) to allow safe
 * integration of instanced buffers without fragile INVOKE mixins.
 */
public interface IRenderBackend {

    /** Called during the initialization phase to setup any necessary OpenGL resources. */
    void init();

    /**
     * Dispatch the instanced draw calls for static entities for a specific chunk or globally.
     * @param mvp       The combined ModelView-Projection matrix.
     * @param tickDelta The current tick delta.
     */
    void dispatchInstancedDraws(@NotNull org.joml.Matrix4fc mvp, float tickDelta);

    /**
     * Submit dynamic block entities that cannot be instanced natively.
     */
    void renderDynamicEntity(@NotNull BlockEntityRenderState renderState,
                             @NotNull PoseStack matrices,
                             @NotNull SubmitNodeCollector collector,
                             @NotNull CameraRenderState cameraRenderState);

    /** Called when the backend shuts down. */
    void destroy();
}
