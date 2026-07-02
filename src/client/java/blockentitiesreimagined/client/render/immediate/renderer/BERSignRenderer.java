package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERSignRenderer implements IInstancedRenderer<SignRenderState> {

    @Override
    public boolean renderState(@NotNull SignRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        if (state.blockPos != null && cameraRenderState.pos != null) {
            double dx = state.blockPos.getX() + 0.5 - cameraRenderState.pos.x;
            double dy = state.blockPos.getY() + 0.5 - cameraRenderState.pos.y;
            double dz = state.blockPos.getZ() + 0.5 - cameraRenderState.pos.z;
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq <= 64.0 * 64.0) {
                return false; // Render via vanilla when close to draw text
            }
        }
        return renderStateGeneric(state, matrices, collector, cameraRenderState);
    }

    @Override
    public boolean isStatic() { return true; }
}
