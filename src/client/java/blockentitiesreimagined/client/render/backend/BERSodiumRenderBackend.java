package blockentitiesreimagined.client.render.backend;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.level.block.entity.BlockEntity;
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
    public void renderDynamicEntity(@NotNull BlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        // Delegate to immediate/dynamic renderer
    }

    @Override
    public void destroy() {
        // Release Sodium chunk data integrations
    }
}
