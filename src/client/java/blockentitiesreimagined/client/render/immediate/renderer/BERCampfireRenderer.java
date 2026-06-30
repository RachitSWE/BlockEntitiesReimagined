package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

/* joml */
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

public class BERCampfireRenderer implements IInstancedRenderer<CampfireBlockEntity> {
    
    @Override
    public void render(@NotNull CampfireBlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        Direction direction = entity.getBlockState().getValue(CampfireBlock.FACING);
        NonNullList<ItemStack> items = entity.getItems();
        int seed = (int) entity.getBlockPos().asLong();
        
        for (int i = 0; i < items.size(); ++i) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                matrices.pushPose();
                matrices.translate(0.5F, 0.44921875F, 0.5F);
                
                Direction dir2 = Direction.from2DDataValue((i + direction.get2DDataValue()) % 4);
                float yRot = -dir2.toYRot();
                Quaternionf rotY = BERMath.getQuat().rotationY((float) Math.toRadians(yRot));
                matrices.mulPose(rotY);
                
                matrices.translate(0.0F, 0.0F, 0.3125F);
                
                Quaternionf rotX = BERMath.getQuat().rotationX((float) Math.toRadians(90.0F));
                matrices.mulPose(rotX);
                
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
        }
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
