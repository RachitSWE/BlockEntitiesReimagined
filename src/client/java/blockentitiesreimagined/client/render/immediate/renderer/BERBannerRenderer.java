package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERBannerRenderer implements IInstancedRenderer<BannerBlockEntity> {
    @Override
    public void render(@NotNull BannerBlockEntity banner, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        emitBannerCloth(matrices, vertexConsumers, banner);
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    private void emitBannerCloth(@NotNull PoseStack matrices, @NotNull SubmitNodeCollector consumers, @NotNull BannerBlockEntity banner) {}
}
