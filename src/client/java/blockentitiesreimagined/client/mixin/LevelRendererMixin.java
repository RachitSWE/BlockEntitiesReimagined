package blockentitiesreimagined.client.mixin;

/* local */
import blockentitiesreimagined.client.render.instanced.BERStaticEntityInstancer;
import blockentitiesreimagined.client.render.backend.BERVanillaRenderBackend;

/* minecraft */
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.culling.Frustum;

/* mojang */
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;

/* mixin */
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* java/joml */
import org.joml.Matrix4fc;
import org.joml.Vector4f;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(at = @At("HEAD"), method = "render")
    private void ber_beforeRender(
            GraphicsResourceAllocator graphicsResourceAllocator, 
            DeltaTracker deltaTracker, 
            boolean renderBlockOutline, 
            CameraRenderState cameraRenderState, 
            Matrix4fc frustumMatrix, 
            GpuBufferSlice fog, 
            Vector4f clearColor, 
            boolean drawSky, 
            CallbackInfo ci) {
        
        if (cameraRenderState.cullFrustum != null && blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer() != null) {
            Frustum frustum = cameraRenderState.cullFrustum;
            blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer().processAndUpload(frustum);
        }
    }

    @Inject(at = @At("HEAD"), method = "submitBlockEntities")
    private void ber_beforeSubmitBlockEntities(
            com.mojang.blaze3d.vertex.PoseStack poseStack, 
            net.minecraft.client.renderer.state.level.LevelRenderState levelRenderState, 
            net.minecraft.client.renderer.SubmitNodeCollector SubmitNodeCollector, 
            CallbackInfo ci) {
        
        // Filter out block entities that are already handled by our instanced renderer
        levelRenderState.blockEntityRenderStates.removeIf(state -> {
            if (state.blockPos != null && state.blockEntityType != null) {
                blockentitiesreimagined.client.api.IInstancedRenderer<?> renderer = 
                    blockentitiesreimagined.client.render.immediate.BERRendererRegistry.getByType(state.blockEntityType);
                if (renderer != null && renderer.isStatic()) {
                    return true;
                }
            }
            return false; 
        });
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void ber_afterRender(
            GraphicsResourceAllocator graphicsResourceAllocator, 
            DeltaTracker deltaTracker, 
            boolean renderBlockOutline, 
            CameraRenderState cameraRenderState, 
            Matrix4fc frustumMatrix, 
            GpuBufferSlice fog, 
            Vector4f clearColor, 
            boolean drawSky, 
            CallbackInfo ci) {
        
        // Dispatch the instanced draws for the static block entities
        if (blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getBackend() != null) {
            blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getBackend().dispatchInstancedDraws(frustumMatrix, deltaTracker.getGameTimeDeltaTicks());
        }
    }
}
