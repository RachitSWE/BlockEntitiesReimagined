package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERDecoratedPotRenderer implements IInstancedRenderer<DecoratedPotRenderState> {

    @Override
    public void renderState(@NotNull DecoratedPotRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // Fully static entity
    }

    @Override
    public boolean isStatic() { return true; }
}
