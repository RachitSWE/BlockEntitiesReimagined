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
    @Unique
    private static final BERVanillaRenderBackend BACKEND = new BERVanillaRenderBackend();
    @Unique
    private static final BERStaticEntityInstancer INSTANCER = new BERStaticEntityInstancer(BACKEND, 10000);

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
        
        if (cameraRenderState.cullFrustum != null) {
            Frustum frustum = cameraRenderState.cullFrustum;
            INSTANCER.processAndUpload(frustum);
        }
    }

    @Inject(at = @At("HEAD"), method = "submitBlockEntities")
    private void ber_beforeSubmitBlockEntities(
            com.mojang.blaze3d.vertex.PoseStack poseStack, 
            net.minecraft.client.renderer.state.level.LevelRenderState levelRenderState, 
            net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, 
            CallbackInfo ci) {
        
        // Filter out block entities that are already handled by our instanced renderer
        levelRenderState.blockEntityRenderStates.removeIf(state -> {
            // Check if the block entity at this position is static and optimized
            if (state.blockPos != null) {
                // We can check if there is an optimized static renderer for this block entity type
                // For safety and simplicity, we can query our registry
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
        BACKEND.dispatchInstancedDraws(null, deltaTracker.getGameTimeDeltaTicks());
    }
}
