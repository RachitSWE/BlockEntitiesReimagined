package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERCopperGolemStatueRenderer implements IInstancedRenderer<CopperGolemStatueBlockEntity> {
    
    @Override
    public void render(@NotNull CopperGolemStatueBlockEntity statue, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers) {
        // Fully static entity - meshes baked directly into chunk geometry
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
