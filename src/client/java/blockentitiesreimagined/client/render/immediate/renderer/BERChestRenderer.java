package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERChestRenderer implements IInstancedRenderer<ChestRenderState> {
    private static final float MAX_OPENING_ANGLE = -1.5707964f;

    @Override
    public boolean renderState(@NotNull ChestRenderState state, @NotNull PoseStack matrices,
                               @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        if (state.open > 0.0f) {
            return false; // Fall back to vanilla rendering when chest is animating
        }
        return renderStateGeneric(state, matrices, collector, cameraRenderState);
    }

    @Override
    public boolean isStatic() { return false; }
}
