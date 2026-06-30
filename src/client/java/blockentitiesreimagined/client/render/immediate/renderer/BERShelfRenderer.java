package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

/* joml */
import com.mojang.blaze3d.vertex.PoseStack;

public class BERShelfRenderer implements IInstancedRenderer<ShelfBlockEntity> {
    
    @Override
    public void render(@NotNull ShelfBlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        int seed = (int) entity.getBlockPos().asLong();
        int i = 0;
        
        for (ItemStack stack : entity.getItems()) {
            if (stack != null && !stack.isEmpty()) {
                matrices.pushPose();
                matrices.translate(0.5F, 0.5F, 0.5F);
                matrices.translate((i % 2) * 0.25F - 0.125F, (i / 2) * 0.25F, 0.0F);
                
                // Minecraft.getInstance().itemRenderer.renderStatic(
                //    stack, 
                //    ItemDisplayContext.FIXED, 
                //    15728880, 
                //    OverlayTexture.NO_OVERLAY, 
                //    matrices, 
                //    vertexConsumers, 
                //    entity.getLevel(), 
                //    seed + i
                // );
                
                matrices.popPose();
            }
            i++;
        }
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
