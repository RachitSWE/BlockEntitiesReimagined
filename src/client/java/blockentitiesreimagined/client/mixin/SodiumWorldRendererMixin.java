package blockentitiesreimagined.client.mixin;

/* local */
import blockentitiesreimagined.client.render.immediate.BERRendererRegistry;
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.render.instanced.BERStaticEntityInstancer;
import blockentitiesreimagined.client.render.backend.BERSodiumRenderBackend;

/* minecraft */
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.BlockDestructionProgress;
import com.mojang.blaze3d.vertex.PoseStack;

/* sodium */
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;

/* mixin */
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* java/misc */
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.SortedSet;

@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class SodiumWorldRendererMixin {

    @Inject(method = "extractBlockEntity", at = @At("HEAD"), cancellable = true)
    private void ber_extractBlockEntity(
            BlockEntity blockEntity, 
            PoseStack poseStack, 
            Camera camera, 
            float tickDelta, 
            Long2ObjectMap<SortedSet<BlockDestructionProgress>> progression, 
            LevelRenderState levelRenderState, 
            boolean globalBlockEntity, 
            CallbackInfo ci) {
        
        IInstancedRenderer<?> renderer = BERRendererRegistry.getByType(blockEntity.getType());
        if (renderer != null && renderer.isStatic()) {
            if (blockEntity.getLevel() != null && blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer() != null) {
                LevelChunk chunk = blockEntity.getLevel().getChunkAt(blockEntity.getBlockPos());
                if (chunk != null) {
                    blockentitiesreimagined.client.BlockEntitiesReimaginedClient.getInstancer().addEntity(chunk, blockEntity);
                }
            }
            ci.cancel();
        }
    }
}
