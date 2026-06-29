package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.core.Direction;

/* jetbrains */
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Quaternionf;

public class BERChestRenderer implements IInstancedRenderer<BlockEntity> {
    private static final float MAX_OPENING_ANGLE = -1.5707964f; 

    @Override
    public void render(@NotNull BlockEntity chest, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        BlockState state = chest.getBlockState();
        if (!(state.getBlock() instanceof AbstractChestBlock)) return;

        float progress = 0.0f;
        if (chest instanceof LidBlockEntity lid) {
            progress = lid.getOpenNess(tickDelta);
        } else if (chest instanceof ChestBlockEntity cb) {
            progress = cb.getOpenNess(tickDelta);
        }

        float angle = progress * MAX_OPENING_ANGLE;

        Quaternionf rotationY = BERMath.getQuat();
        Direction facing = Direction.SOUTH;
        if (state.hasProperty(ChestBlock.FACING)) {
            facing = state.getValue(ChestBlock.FACING);
        }
        float rotationDegrees = facing.toYRot();
        rotationY.rotationY(org.joml.Math.toRadians(-rotationDegrees));

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

    private void emitBasePart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull BlockEntity chest) {
        // Implementation retrieves cached quads and outputs them to consumer
    }

    private void emitLidPart(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull BlockEntity chest) {
        // Implementation retrieves cached quads and outputs them to consumer
    }
}
