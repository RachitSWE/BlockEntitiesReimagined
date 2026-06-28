package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/* joml */
import org.joml.Math;

public class BERSignRenderer implements IInstancedRenderer<SignBlockEntity> {
    private static final float ROT_180 = Math.toRadians(180f);

    @Override
    public void render(@NotNull SignBlockEntity sign, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        SignText frontText = sign.getFrontText();
        SignText backText = sign.getBackText();

        matrices.pushPose();
        renderText(frontText, matrices, vertexConsumers);
        
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.mulPose(BERMath.getQuat().rotationY(ROT_180));
        matrices.translate(-0.5f, -0.5f, -0.5f);
        
        renderText(backText, matrices, vertexConsumers);
        matrices.popPose();
    }

    @Override
    public boolean isStatic() {
        return true; 
    }

    private void renderText(@NotNull SignText text, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers) {}
}
