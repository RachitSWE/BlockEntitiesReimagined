package blockentitiesreimagined.client.render.instanced;

/* local */
import blockentitiesreimagined.client.api.IRenderBackend;
import blockentitiesreimagined.client.math.BERMath;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.client.renderer.culling.Frustum;

/* java */
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;
import com.google.common.collect.MapMaker;
import org.jetbrains.annotations.NotNull;

/**
 * Manages tracking and frustum culling of static entities before dispatching to the IRenderBackend.
 * Binds entities to LevelChunks using WeakHashMap to prevent memory leaks,
 * and groups them per chunk to allow fast spatial iteration.
 */
public class BERStaticEntityInstancer {
    
    private final ConcurrentMap<LevelChunk, ChunkEntityData> chunkMap = new MapMaker()
            .weakKeys()
            .concurrencyLevel(4)
            .makeMap();
    
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
        
        for (Map.Entry<LevelChunk, ChunkEntityData> entry : chunkMap.entrySet()) {
            LevelChunk chunk = entry.getKey();
            ChunkEntityData data = entry.getValue();
            
            if (data == null || chunk == null) continue;
            
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
                    blockentitiesreimagined.client.api.IInstancedRenderer<?> renderer =
                            blockentitiesreimagined.client.render.immediate.BERRendererRegistry.getByType(entity.getType());
                    
                    if (renderer != null && renderer.isStatic()) {
                        net.minecraft.core.BlockPos pos = entity.getBlockPos();
                        net.minecraft.world.phys.AABB aabb = new net.minecraft.world.phys.AABB(
                                pos.getX(), pos.getY(), pos.getZ(),
                                pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0
                        );
                        
                        if (frustum.isVisible(aabb)) {
                            org.joml.Matrix4f matrix = BERMath.getMat4().translation(pos.getX(), pos.getY(), pos.getZ());
                            instancedBuffer.addInstance(matrix);
                        }
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
