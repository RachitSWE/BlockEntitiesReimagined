package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERLecternRenderer implements IInstancedRenderer<LecternRenderState> {

    @Override
    public boolean renderState(@NotNull LecternRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        if (state.hasBook) {
            return false; // Fall back to vanilla rendering when lectern has a book
        }
        return renderStateGeneric(state, matrices, collector, cameraRenderState);
    }

    @Override
    public boolean isStatic() { return false; }
}
