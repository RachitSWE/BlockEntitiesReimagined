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
        // state.open is the lid openness [0..1], state.facing is the chest orientation
        float angle = state.open * MAX_OPENING_ANGLE;
        Direction facing = state.facing != null ? state.facing : Direction.SOUTH;

        Quaternionf rotationY = BERMath.getQuat();
        rotationY.rotationY(org.joml.Math.toRadians(-facing.toYRot()));

        matrices.pushPose();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(rotationY);
        matrices.translate(-0.5f, -0.5f, -0.5f);

        emitBasePart(matrices, collector, state);

        if (angle != 0.0f) {
            matrices.pushPose();
            matrices.translate(0f, 0.5625f, 0.0625f);
            Quaternionf rotationX = BERMath.getQuat().rotationX(angle);
            matrices.mulPose(rotationX);
            matrices.translate(0f, -0.5625f, -0.0625f);
            emitLidPart(matrices, collector, state);
            matrices.popPose();
        } else {
            emitLidPart(matrices, collector, state);
        }

        matrices.popPose();
        return false; // stubs not yet implemented — fall through to vanilla
    }

    @Override
    public boolean isStatic() { return false; }

    private void emitBasePart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers,
                              @NotNull ChestRenderState state) {
        // Implementation retrieves cached quads and outputs them to consumer
    }

    private void emitLidPart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers,
                             @NotNull ChestRenderState state) {
        // Implementation retrieves cached quads and outputs them to consumer
    }
}
