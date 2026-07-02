package blockentitiesreimagined.client.mixin;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.render.BERFrustumCuller;
import blockentitiesreimagined.client.render.immediate.BERRendererRegistry;

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
 * Intercepts {@code BlockEntityRenderDispatcher.submit()} — the MC 26.2 render-graph
 * submission point that replaces the old {@code render()} method.
 *
 * <h2>Win 1 — Frustum Culling</h2>
 * <p>Applied before ANY registry lookup. If the block entity's 1×1×1 AABB is outside the
 * camera frustum, {@code submit()} is cancelled immediately — zero geometry work on the CPU,
 * zero GPU submission. Uses {@link BERFrustumCuller} for a zero-allocation test path via
 * the access-widened {@code Frustum.intersection} field.</p>
 *
 * <h2>Win 2 / 3 dispatch</h2>
 * <p>When BER has a registered renderer for a block entity type, we delegate to it.
 * The renderer returns {@code true} to cancel vanilla, {@code false} to fall through.
 * Stubs always return {@code false} so vanilla renders normally during development.</p>
 */
@Mixin(value = BlockEntityRenderDispatcher.class, priority = 1500)
public class BlockEntityRenderDispatcherMixin {

    @org.spongepowered.asm.mixin.Shadow
    private java.util.Map<net.minecraft.world.level.block.entity.BlockEntityType<?>, net.minecraft.client.renderer.blockentity.BlockEntityRenderer<?, ?>> renderers;

    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntityRenderState> void ber_interceptSubmit(
            S renderState,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState cameraRenderState,
            CallbackInfo ci) {

        // ── WIN 1: Frustum Culling ──────────────────────────────────────────
        // Applied universally — even for block entity types we don't have a
        // custom renderer for. Cancels vanilla submit() for off-screen entities.
        // Uses zero-allocation BERFrustumCuller (no new AABB per call).
        if (cameraRenderState.cullFrustum != null && renderState.blockPos != null) {
            if (!BERFrustumCuller.isVisible(cameraRenderState.cullFrustum, renderState.blockPos)) {
                ci.cancel();
                return;
            }
        }

        // ── WIN 2 / 3: Custom Renderer Dispatch ────────────────────────────
        // Only runs for block entity types registered with BER.
        if (renderState.blockEntityType == null) return;

        if (blockentitiesreimagined.client.render.immediate.BERRendererRegistry.getVanillaRenderer(renderState.blockEntityType) == null) {
            blockentitiesreimagined.client.render.immediate.BERRendererRegistry.setVanillaRenderers((java.util.Map) renderers);
        }

        IInstancedRenderer<S> renderer = BERRendererRegistry.getByType(renderState.blockEntityType);
        if (renderer == null) return;

        boolean handled = renderer.renderState(renderState, poseStack, submitNodeCollector, cameraRenderState);
        if (handled) {
            ci.cancel();
        }
    }
}
