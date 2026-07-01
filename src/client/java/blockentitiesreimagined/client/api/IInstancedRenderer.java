package blockentitiesreimagined.client.api;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/**
 * Implemented by each BER renderer.
 *
 * <p>In Minecraft 26.2 the block-entity rendering pipeline is two-phase:
 * <ol>
 *   <li>{@link net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher#tryExtractRenderState}
 *       snapshots live data into an immutable {@link BlockEntityRenderState}.</li>
 *   <li>{@link net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher#submit}
 *       draws that snapshot on the render thread.</li>
 * </ol>
 * BER intercepts phase 2 ({@code submit}) and delegates here.</p>
 *
 * @param <S> the concrete {@link BlockEntityRenderState} subtype for this renderer
 */
public interface IInstancedRenderer<S extends BlockEntityRenderState> {

    /**
     * Render using the already-extracted render state (phase 2).
     * Called from {@code BlockEntityRenderDispatcherMixin} instead of vanilla {@code submit}.
     *
     * @return {@code true} if this renderer fully handled rendering and vanilla should be
     *         suppressed; {@code false} to fall through to vanilla (use during stub / WIP
     *         implementation to prevent invisible blocks).
     */
    boolean renderState(
            @NotNull S state,
            @NotNull PoseStack matrices,
            @NotNull SubmitNodeCollector collector,
            @NotNull CameraRenderState cameraRenderState);

    /** @return {@code true} if this renderer produces fully static geometry suitable for instancing. */
    boolean isStatic();
}
