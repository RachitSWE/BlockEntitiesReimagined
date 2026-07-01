package blockentitiesreimagined.client.mixin;

/* local */
import blockentitiesreimagined.client.render.immediate.BERRendererRegistry;
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/* spongepowered */
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercepts BlockEntityRenderDispatcher.submit() — the MC 26.2 render-graph
 * submission point that replaces the old render() method.
 *
 * <p>When BER has a registered renderer for a block entity type, we cancel the
 * vanilla submission and delegate to our optimised renderer instead.</p>
 */
@Mixin(value = BlockEntityRenderDispatcher.class, priority = 1500)
public class BlockEntityRenderDispatcherMixin {

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntityRenderState> void ber_interceptSubmit(
            S renderState,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState,
            CallbackInfo ci) {

        if (renderState.blockEntityType == null) return;

        IInstancedRenderer<S> renderer = BERRendererRegistry.getByType(renderState.blockEntityType);
        if (renderer == null) return;

        boolean handled = renderer.renderState(renderState, poseStack, submitNodeCollector, cameraRenderState);
        if (handled) {
            ci.cancel();
        }
    }
}
