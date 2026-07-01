package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.CopperGolemStatueRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERCopperGolemStatueRenderer implements IInstancedRenderer<CopperGolemStatueRenderState> {

    @Override
    public void renderState(@NotNull CopperGolemStatueRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // Fully static entity - meshes baked directly into chunk geometry
    }

    @Override
    public boolean isStatic() { return true; }
}
