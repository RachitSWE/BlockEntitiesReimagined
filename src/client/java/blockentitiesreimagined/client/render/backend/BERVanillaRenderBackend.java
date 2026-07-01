package blockentitiesreimagined.client.render.backend;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.jetbrains.annotations.NotNull;

public class BERVanillaRenderBackend implements IRenderBackend {

    @Override
    public void init() {
        // Initialize OpenGL buffers for Vanilla context
    }

    @Override
    public void dispatchInstancedDraws(@NotNull Camera camera, float tickDelta) {
        // Vanilla global frustum culling and glDrawElementsInstanced dispatch
    }

    @Override
    public void renderDynamicEntity(@NotNull BlockEntityRenderState renderState,
                                    @NotNull PoseStack matrices,
                                    @NotNull SubmitNodeCollector collector,
                                    @NotNull CameraRenderState cameraRenderState) {
        // Fallback or dynamic rendering routing
    }

    @Override
    public void destroy() {
        // Clean up VAOs and VBOs
    }
}
