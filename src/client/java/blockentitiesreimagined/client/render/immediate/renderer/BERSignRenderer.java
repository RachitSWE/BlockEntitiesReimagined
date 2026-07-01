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
    private static final float ROT_180 = org.joml.Math.toRadians(180f);

    @Override
    public void renderState(@NotNull SignRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        matrices.pushPose();
        renderTextSide(state, matrices, collector, false);

        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(BERMath.getQuat().rotationY(ROT_180));
        matrices.translate(-0.5f, -0.5f, -0.5f);

        renderTextSide(state, matrices, collector, true);
        matrices.popPose();
    }

    @Override
    public boolean isStatic() { return true; }

    private void renderTextSide(@NotNull SignRenderState state, @NotNull PoseStack matrices,
                                @NotNull SubmitNodeCollector consumers, boolean back) {}
}
