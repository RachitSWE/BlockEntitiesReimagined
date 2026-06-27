package blockentitiesreimagined.client.render.immediate.blockentity.manager;

/* guava */
import com.google.common.collect.MapMaker;

/* java */
import java.util.concurrent.ConcurrentMap;

/* jetbrains */
import org.jetbrains.annotations.NotNull;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;

public final class InstancedBlockEntityManager {
    public enum Phase {
        IDLE,
        IMMEDIATE_ACTIVE,
        WAITING_TERRAIN
    }

    private static final ConcurrentMap<BlockEntity, InstancedBlockEntityManager> MANAGERS = 
            new MapMaker().weakKeys().concurrencyLevel(4).makeMap();

    private volatile Phase phase = Phase.IDLE;
    private boolean animating;
    private boolean pendingRebuild;

    private InstancedBlockEntityManager() {}

    public static InstancedBlockEntityManager getOrCreate(@NotNull BlockEntity entity) {
        InstancedBlockEntityManager existing = MANAGERS.get(entity);
        if (existing != null) return existing;
        return MANAGERS.computeIfAbsent(entity, e -> new InstancedBlockEntityManager());
    }

    public static boolean isBaked(@NotNull BlockEntity entity) {
        InstancedBlockEntityManager manager = MANAGERS.get(entity);
        return manager == null || manager.getPhase() != Phase.IMMEDIATE_ACTIVE;
    }

    public Phase getPhase() {
        return phase;
    }

    public synchronized void setAnimating(boolean animating) {
        this.animating = animating;
        if (animating) {
            phase = Phase.IMMEDIATE_ACTIVE;
        } else {
            phase = pendingRebuild ? Phase.WAITING_TERRAIN : Phase.IDLE;
        }
    }

    public synchronized void requestRebuild() {
        pendingRebuild = true;
        if (!animating) {
            phase = Phase.WAITING_TERRAIN;
        }
    }

    public synchronized void onRebuildComplete() {
        pendingRebuild = false;
        if (phase == Phase.WAITING_TERRAIN) {
            phase = Phase.IDLE;
        }
    }
}
