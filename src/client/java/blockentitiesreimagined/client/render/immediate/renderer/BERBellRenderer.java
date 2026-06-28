package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Quaternionf;

public class BERBellRenderer implements IInstancedRenderer<BellBlockEntity> {
    @Override
    public void render(@NotNull BellBlockEntity bell, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        if (!bell.shaking) {
            emitStaticBell(matrices, vertexConsumers, bell);
            return;
        }

        float progress = (float) bell.ticks + tickDelta;
        float angle = Mth.sin(progress / (float) Math.PI) / (4.0f + progress / 3.0f);

        Direction facing = bell.clickDirection != null ? bell.clickDirection : Direction.NORTH;
        Quaternionf rotation = BERMath.getQuat();

        matrices.pushPose();
        matrices.translate(0.5f, 0.75f, 0.5f);
        
        switch (facing) {
            case EAST -> rotation.fromAxisAngleRad(0f, 0f, 1f, -angle);
            case WEST -> rotation.fromAxisAngleRad(0f, 0f, 1f, angle);
            case SOUTH -> rotation.fromAxisAngleRad(1f, 0f, 0f, -angle);
            default -> rotation.fromAxisAngleRad(1f, 0f, 0f, angle);
        }

        matrices.mulPose(rotation);
        matrices.translate(-0.5f, -0.75f, -0.5f);
        emitStaticBell(matrices, vertexConsumers, bell);
        matrices.popPose();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    private void emitStaticBell(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull BellBlockEntity bell) {}
}
