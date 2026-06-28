package blockentitiesreimagined.client.api;

/* minecraft */
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Abstracts the rendering backend (Vanilla vs Sodium) to allow safe 
 * integration of instanced buffers without fragile INVOKE mixins.
 */
public interface IRenderBackend {
    
    /**
     * Called during the initialization phase to setup any necessary OpenGL resources.
     */
    void init();
    
    /**
     * Dispatch the instanced draw calls for static entities for a specific chunk or globally.
     * @param camera The current camera.
     * @param tickDelta The current tick delta.
     */
    void dispatchInstancedDraws(@NotNull Camera camera, float tickDelta);
    
    /**
     * Submit dynamic block entities that cannot be instanced natively.
     */
    void renderDynamicEntity(@NotNull BlockEntity entity, float tickDelta, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector vertexConsumers);
    
    /**
     * Called when the backend shuts down.
     */
    void destroy();
}
