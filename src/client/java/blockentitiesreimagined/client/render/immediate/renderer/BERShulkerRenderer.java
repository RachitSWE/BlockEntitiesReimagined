package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERShulkerRenderer implements IInstancedRenderer<ShulkerBoxRenderState> {
    private static final float ROT_180 = org.joml.Math.toRadians(180f);
    private static final float ROT_90  = org.joml.Math.toRadians(90f);
    private static final float ROT_N90 = org.joml.Math.toRadians(-90f);

    @Override
    public boolean renderState(@NotNull ShulkerBoxRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        if (state.progress > 0.0f) {
            return false; // Fall back to vanilla rendering when shulker box is opening/closing
        }
        return renderStateGeneric(state, matrices, collector, cameraRenderState);
    }

    @Override
    public boolean isStatic() { return false; }
}
