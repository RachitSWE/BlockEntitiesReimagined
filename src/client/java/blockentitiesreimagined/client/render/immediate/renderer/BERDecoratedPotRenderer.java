package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

/* jetbrains */
import org.jetbrains.annotations.NotNull;

public class BERDecoratedPotRenderer implements IInstancedRenderer<DecoratedPotBlockEntity> {
    @Override
    public void render(@NotNull DecoratedPotBlockEntity pot, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        // Fully static entity
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
