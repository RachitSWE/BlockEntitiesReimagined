package blockentitiesreimagined.client.mixin;

/* local */
import blockentitiesreimagined.client.render.immediate.BERRendererRegistry;
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;

/* spongepowered */
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockEntityRenderDispatcher.class, priority = 1500)
public class BlockEntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void ber_interceptRender(
            E blockEntity,
            float tickDelta,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CallbackInfo ci) {
            
        IInstancedRenderer<BlockEntity> renderer = BERRendererRegistry.get(blockEntity);
        if (renderer == null) {
            return; 
        }

        renderer.render(blockEntity, tickDelta, poseStack, submitNodeCollector);
        
        ci.cancel();
    }
}
