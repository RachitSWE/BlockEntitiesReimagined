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

public class BERSodiumRenderBackend implements IRenderBackend {

    @Override
    public void init() {
        // Safe hook to Sodium chunk builder resources
    }

    @Override
    public void dispatchInstancedDraws(@NotNull Camera camera, float tickDelta) {
        // Trigger Sodium-specific render graph batch execution
    }

    @Override
    public void renderDynamicEntity(@NotNull BlockEntityRenderState renderState,
                                    @NotNull PoseStack matrices,
                                    @NotNull SubmitNodeCollector collector,
                                    @NotNull CameraRenderState cameraRenderState) {
        // Delegate to immediate/dynamic renderer
    }

    @Override
    public void destroy() {
        // Release Sodium chunk data integrations
    }
}
