package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BellRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERBellRenderer implements IInstancedRenderer<BellRenderState> {

    @Override
    public boolean renderState(@NotNull BellRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // state.ticks == 0 means not ringing; shakeDirection is null when static
        if (state.shakeDirection != null) {
            return false;
        }
        return renderStateGeneric(state, matrices, collector, cameraRenderState);
    }

    @Override
    public boolean isStatic() { return false; }
}
