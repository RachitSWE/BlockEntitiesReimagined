package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Quaternionf;

public class BERShulkerRenderer implements IInstancedRenderer<ShulkerBoxBlockEntity> {
    private static final float ROT_180 = org.joml.Math.toRadians(180f);
    private static final float ROT_90 = org.joml.Math.toRadians(90f);
    private static final float ROT_N90 = org.joml.Math.toRadians(-90f);

    @Override
    public void render(@NotNull ShulkerBoxBlockEntity shulker, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        BlockState state = shulker.getBlockState();
        if (!(state.getBlock() instanceof ShulkerBoxBlock)) return;

        float progress = shulker.getProgress(tickDelta);
        Direction facing = state.getValue(ShulkerBoxBlock.FACING);

        matrices.pushPose();
        if (facing != Direction.UP) {
            matrices.translate(0.5f, 0.5f, 0.5f);
            Quaternionf rotation = BERMath.getQuat();
            switch (facing) {
                case DOWN -> rotation.rotationX(ROT_180);
                case NORTH -> rotation.rotationX(ROT_N90);
                case SOUTH -> rotation.rotationX(ROT_90);
                case WEST -> rotation.rotationZ(ROT_90);
                case EAST -> rotation.rotationZ(ROT_N90);
                default -> {} 
            }
            matrices.mulPose(rotation);
            matrices.translate(-0.5f, -0.5f, -0.5f);
        }

        emitBase(matrices, vertexConsumers, shulker);

        float lidTranslation = progress * 0.5f;
        if (lidTranslation != 0.0f) {
            matrices.pushPose();
            matrices.translate(0.0f, lidTranslation, 0.0f);
            emitLid(matrices, vertexConsumers, shulker);
            matrices.popPose();
        } else {
            emitLid(matrices, vertexConsumers, shulker);
        }

        matrices.popPose();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    private void emitBase(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull ShulkerBoxBlockEntity shulker) {}
    private void emitLid(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull ShulkerBoxBlockEntity shulker) {}
}
