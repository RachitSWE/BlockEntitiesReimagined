package blockentitiesreimagined.client.render.backend;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.level.block.entity.BlockEntity;
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
    public void renderDynamicEntity(@NotNull BlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        // Fallback or dynamic rendering routing
    }

    @Override
    public void destroy() {
        // Clean up VAOs and VBOs
    }
}
