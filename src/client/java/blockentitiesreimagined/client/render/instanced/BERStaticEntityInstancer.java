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
    
    private final java.util.concurrent.ConcurrentHashMap<net.minecraft.world.level.block.entity.BlockEntityType<?>, BERInstancedBuffer> buffers =
            new java.util.concurrent.ConcurrentHashMap<>();
    private final IRenderBackend backend;
    private final int maxInstances;

    public BERStaticEntityInstancer(IRenderBackend backend, int maxInstances) {
        this.backend = backend;
        this.maxInstances = maxInstances;
    }

    public BERInstancedBuffer getBuffer(net.minecraft.world.level.block.entity.BlockEntityType<?> type) {
        return buffers.computeIfAbsent(type, t -> new BERInstancedBuffer(maxInstances));
    }

    public java.util.concurrent.ConcurrentHashMap<net.minecraft.world.level.block.entity.BlockEntityType<?>, BERInstancedBuffer> getBuffers() {
        return buffers;
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
        // Reset all active buffers
        for (BERInstancedBuffer buffer : buffers.values()) {
            buffer.reset();
        }
        
        for (Map.Entry<LevelChunk, ChunkEntityData> entry : chunkMap.entrySet()) {
            ChunkEntityData data = entry.getValue();
            if (data.dirty) {
                synchronized (data) {
                    if (data.dirty) {
                        data.fastArray = data.entities.toArray(new BlockEntity[0]);
                        data.dirty = false;
                    }
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
                        
                        // inv: index i represents already processed entities; instancedBuffer contains matrices for visible entities in array[0..i).
                        if (blockentitiesreimagined.client.render.BERFrustumCuller.isVisible(frustum, pos)) {
                            org.joml.Matrix4f matrix = BERMath.getMat4().translation(pos.getX(), pos.getY(), pos.getZ());
                            getBuffer(entity.getType()).addInstance(matrix);
                        }
                    }
                }
            }
        }
        
        // Upload instances to GPU for all active buffers
        for (BERInstancedBuffer buffer : buffers.values()) {
            buffer.uploadInstances();
        }
    }
    
    private static class ChunkEntityData {
        final Set<BlockEntity> entities = new HashSet<>();
        volatile BlockEntity[] fastArray = new BlockEntity[0];
        volatile boolean dirty = false;
    }
}
