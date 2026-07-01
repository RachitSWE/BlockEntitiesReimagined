package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BellRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERBellRenderer implements IInstancedRenderer<BellRenderState> {

    @Override
    public void renderState(@NotNull BellRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // state.ticks == 0 means not ringing; shakeDirection is null when static
        if (state.shakeDirection == null) {
            emitStaticBell(matrices, collector, state);
            return;
        }

        float progress = state.ticks;
        float angle = Mth.sin(progress / (float) Math.PI) / (4.0f + progress / 3.0f);

        Direction facing = state.shakeDirection;
        Quaternionf rotation = BERMath.getQuat();

        matrices.pushPose();
        matrices.translate(0.5f, 0.75f, 0.5f);

        switch (facing) {
            case EAST  -> rotation.fromAxisAngleRad(0f, 0f, 1f, -angle);
            case WEST  -> rotation.fromAxisAngleRad(0f, 0f, 1f,  angle);
            case SOUTH -> rotation.fromAxisAngleRad(1f, 0f, 0f, -angle);
            default    -> rotation.fromAxisAngleRad(1f, 0f, 0f,  angle);
        }

        matrices.mulPose(rotation);
        matrices.translate(-0.5f, -0.75f, -0.5f);
        emitStaticBell(matrices, collector, state);
        matrices.popPose();
    }

    @Override
    public boolean isStatic() { return false; }

    private void emitStaticBell(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers,
                                @NotNull BellRenderState state) {}
}
