package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.CampfireRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class BERCampfireRenderer implements IInstancedRenderer<CampfireRenderState> {

    @Override
    public boolean renderState(@NotNull CampfireRenderState state, @NotNull PoseStack matrices,
                            @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState cameraRenderState) {
        // Items on the campfire are handled via instanced item rendering
        // Static log/frame geometry is baked into the chunk mesh
        return false;
    }

    @Override
    public boolean isStatic() { return false; }
}
