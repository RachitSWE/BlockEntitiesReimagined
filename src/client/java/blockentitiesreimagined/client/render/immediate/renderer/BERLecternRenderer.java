package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.core.Direction;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Quaternionf;

public class BERLecternRenderer implements IInstancedRenderer<LecternBlockEntity> {
    
    @Override
    public void render(@NotNull LecternBlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        if (!entity.hasBook()) {
            return;
        }

        @NotNull Direction direction = entity.getBlockState().getValue(LecternBlock.FACING);
        
        matrices.pushPose();
        matrices.translate(0.5F, 1.0625F, 0.5F);
        
        float yRot = -direction.toYRot();
        Quaternionf rotY = BERMath.getQuat().rotationY((float) Math.toRadians(yRot));
        matrices.mulPose(rotY);
        
        matrices.translate(0.0F, -0.125F, 0.0F);
        
        Quaternionf rotZ = BERMath.getQuat().rotationZ((float) Math.toRadians(67.5F));
        matrices.mulPose(rotZ);
        
        matrices.translate(0.0F, -0.2265625F, 0.0F);
        
        // Setup dynamic book animations (Vanilla logic)
        // float time = entity.getBookAnimationTime(tickDelta);
        // float lastTime = entity.getBookAnimationTime(tickDelta - 1.0F);
        // this.bookModel.setupAnim(time, Math.max(0.0F, time - lastTime), tickDelta, 0.0F);
        
        // Submit geometry to the collector
        // VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.entitySolid(net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS));
        // this.bookModel.renderToBuffer(matrices, buffer, 15728880, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        
        matrices.popPose();
    }

    @Override
    public boolean isStatic() {
        return false;
    }
}
