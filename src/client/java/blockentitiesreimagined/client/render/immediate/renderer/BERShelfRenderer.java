package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.ShelfRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class BERShelfRenderer implements IInstancedRenderer<ShelfRenderState> {

    @Override
    public void renderState(@NotNull ShelfRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // Item rendering via instanced item pipeline
    }

    @Override
    public boolean isStatic() { return false; }
}
