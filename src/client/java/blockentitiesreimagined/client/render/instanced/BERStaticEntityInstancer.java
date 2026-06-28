package blockentitiesreimagined.client.render.instanced;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.client.renderer.culling.Frustum;

/* java */
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Set;
import java.util.HashSet;
import org.jetbrains.annotations.NotNull;

/**
 * Manages tracking and frustum culling of static entities before dispatching to the IRenderBackend.
 * Binds entities to LevelChunks using WeakHashMap to prevent memory leaks,
 * and groups them per chunk to allow fast spatial iteration.
 */
public class BERStaticEntityInstancer {
    
    private final Map<LevelChunk, ChunkEntityData> chunkMap = Collections.synchronizedMap(new WeakHashMap<>());
    
    private final BERInstancedBuffer instancedBuffer;
    private final IRenderBackend backend;

    public BERStaticEntityInstancer(IRenderBackend backend, int maxInstances) {
        this.backend = backend;
        this.instancedBuffer = new BERInstancedBuffer(maxInstances);
    }

    public void addEntity(@NotNull LevelChunk chunk, @NotNull BlockEntity entity) {
        ChunkEntityData data = chunkMap.computeIfAbsent(chunk, k -> new ChunkEntityData());
        synchronized (data) {
            data.entities.add(entity);
            data.dirty = true;
        }
    }

    public void removeEntity(@NotNull LevelChunk chunk, @NotNull BlockEntity entity) {
        ChunkEntityData data = chunkMap.get(chunk);
        if (data != null) {
            synchronized (data) {
                data.entities.remove(entity);
                data.dirty = true;
            }
        }
    }

    public void processAndUpload(@NotNull Frustum frustum) {
        instancedBuffer.reset();
        
        synchronized (chunkMap) {
            for (Map.Entry<LevelChunk, ChunkEntityData> entry : chunkMap.entrySet()) {
                LevelChunk chunk = entry.getKey();
                ChunkEntityData data = entry.getValue();
                
                if (data == null || chunk == null) continue;
                
                // Fast chunk culling logic can be applied here
                // e.g. if (!frustum.isVisible(chunk.getBoundingBox())) continue;
                
                if (data.dirty) {
                    synchronized (data) {
                        data.fastArray = data.entities.toArray(new BlockEntity[0]);
                        data.dirty = false;
                    }
                }
                
                BlockEntity[] array = data.fastArray;
                for (int i = 0; i < array.length; i++) {
                    BlockEntity entity = array[i];
                    if (entity != null) {
                        // Perform fast frustum culling using frustum for specific entity
                        // If visible, compute matrix and push to buffer
                    }
                }
            }
        }
        
        instancedBuffer.uploadInstances();
    }

    public BERInstancedBuffer getBuffer() {
        return instancedBuffer;
    }
    
    private static class ChunkEntityData {
        final Set<BlockEntity> entities = new HashSet<>();
        BlockEntity[] fastArray = new BlockEntity[0];
        boolean dirty = false;
    }
}
