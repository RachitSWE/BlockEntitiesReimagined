package blockentitiesreimagined.client.render;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.BER;

/* java */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/* jetbrains */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InstancedRenderingEngine {
    private static final Map<BlockEntityType<?>, IInstancedRenderer<?>> RENDERERS = new ConcurrentHashMap<>();
    
    private InstancedRenderingEngine() {}
    
    public static <T extends BlockEntity> void register(@NotNull BlockEntityType<T> type, @NotNull IInstancedRenderer<T> renderer) {
        RENDERERS.put(type, renderer);
        BER.getLogger().info("Registered instanced renderer for {}", type);
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends BlockEntity> IInstancedRenderer<T> getRenderer(@NotNull BlockEntityType<T> type) {
        return (IInstancedRenderer<T>) RENDERERS.get(type);
    }
    
    public static boolean hasRenderer(@NotNull BlockEntityType<?> type) {
        return RENDERERS.containsKey(type);
    }
}
