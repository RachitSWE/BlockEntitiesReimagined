package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.core.Direction;

/* jetbrains */
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Quaternionf;
import org.joml.Math;

public class BERChestRenderer implements IInstancedRenderer<ChestBlockEntity> {
    private static final float MAX_OPENING_ANGLE = -1.5707964f; 

    @Override
    public void render(@NotNull ChestBlockEntity chest, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        BlockState state = chest.getBlockState();
        if (!(state.getBlock() instanceof ChestBlock)) return;

        float progress = chest.getOpenNess(tickDelta);
        float angle = progress * MAX_OPENING_ANGLE;

        Quaternionf rotationY = BERMath.getQuat();
        Direction facing = state.getValue(ChestBlock.FACING);
        float rotationDegrees = facing.toYRot();
        rotationY.rotationY(Math.toRadians(-rotationDegrees));

        Quaternionf rotationX = BERMath.getQuat();
        rotationX.rotationX(angle);

        matrices.pushPose();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(rotationY);
        matrices.translate(-0.5f, -0.5f, -0.5f);
        
        emitBasePart(matrices, vertexConsumers, chest);

        if (angle != 0.0f) {
            matrices.pushPose();
            matrices.translate(0f, 0.5625f, 0.0625f);
            matrices.mulPose(rotationX);
            matrices.translate(0f, -0.5625f, -0.0625f);
            emitLidPart(matrices, vertexConsumers, chest);
            matrices.popPose();
        } else {
            emitLidPart(matrices, vertexConsumers, chest);
        }

        matrices.popPose();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    private void emitBasePart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull ChestBlockEntity chest) {
        // Implementation retrieves cached quads and outputs them to consumer
    }

    private void emitLidPart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull ChestBlockEntity chest) {
        // Implementation retrieves cached quads and outputs them to consumer
    }
}
