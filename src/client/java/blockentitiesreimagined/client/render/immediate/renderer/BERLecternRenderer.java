package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERLecternRenderer implements IInstancedRenderer<LecternRenderState> {

    @Override
    public void renderState(@NotNull LecternRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        if (!state.hasBook) return;

        // Real fields: state.hasBook (boolean), state.yRot (float, pre-computed rotation)
        matrices.pushPose();
        matrices.translate(0.5F, 1.0625F, 0.5F);

        Quaternionf rotY = BERMath.getQuat().rotationY((float) Math.toRadians(state.yRot));
        matrices.mulPose(rotY);

        matrices.translate(0.0F, -0.125F, 0.0F);

        Quaternionf rotZ = BERMath.getQuat().rotationZ((float) Math.toRadians(67.5F));
        matrices.mulPose(rotZ);

        matrices.translate(0.0F, -0.2265625F, 0.0F);
        // Book geometry submission goes here
        matrices.popPose();
    }

    @Override
    public boolean isStatic() { return false; }
}
