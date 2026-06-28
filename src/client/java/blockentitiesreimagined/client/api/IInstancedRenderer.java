package blockentitiesreimagined.client.api;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public interface IInstancedRenderer<T extends BlockEntity> {
    void render(@NotNull T entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers);
    boolean isStatic();
}
