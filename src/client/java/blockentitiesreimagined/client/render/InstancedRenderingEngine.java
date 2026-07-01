package blockentitiesreimagined.client.render;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.BER;

/* java */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntityType;

/* jetbrains */
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Public API engine for third-party mods to register their own block entity renderers.
 * Keyed by {@link BlockEntityType} which is the primary block entity identifier in MC 26.2.
 */
public final class InstancedRenderingEngine {
    private static final Map<BlockEntityType<?>, IInstancedRenderer<?>> RENDERERS = new ConcurrentHashMap<>();

    private InstancedRenderingEngine() {}

    public static <S extends BlockEntityRenderState> void register(
            @NotNull BlockEntityType<?> type,
            @NotNull IInstancedRenderer<S> renderer) {
        RENDERERS.put(type, renderer);
        BER.getLogger().info("Registered instanced renderer for {}", type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <S extends BlockEntityRenderState> IInstancedRenderer<S> getRenderer(
            @NotNull BlockEntityType<?> type) {
        return (IInstancedRenderer<S>) RENDERERS.get(type);
    }

    public static boolean hasRenderer(@NotNull BlockEntityType<?> type) {
        return RENDERERS.containsKey(type);
    }
}
