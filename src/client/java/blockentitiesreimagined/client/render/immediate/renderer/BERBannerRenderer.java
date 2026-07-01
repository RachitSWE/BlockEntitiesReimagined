package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERBannerRenderer implements IInstancedRenderer<BannerRenderState> {

    @Override
    public void renderState(@NotNull BannerRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        emitBannerCloth(matrices, collector, state);
    }

    @Override
    public boolean isStatic() { return true; }

    private void emitBannerCloth(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers,
                                 @NotNull BannerRenderState state) {}
}
