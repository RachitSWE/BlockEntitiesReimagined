package blockentitiesreimagined.client.render.immediate;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.*;

/* minecraft */
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

/* java */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public final class BERRendererRegistry {
    private static final Map<BlockEntityType<?>, IInstancedRenderer<?>> REGISTRY = new ConcurrentHashMap<>();

    static {
        BERChestRenderer chestRenderer = new BERChestRenderer();
        registerByKey("chest", chestRenderer);
        registerByKey("ender_chest", chestRenderer);
        registerByKey("trapped_chest", chestRenderer);

        registerByKey("shulker_box", new BERShulkerRenderer());
        registerByKey("sign", new BERSignRenderer());
        registerByKey("hanging_sign", new BERSignRenderer());
        registerByKey("banner", new BERBannerRenderer());
        registerByKey("bell", new BERBellRenderer());
        registerByKey("decorated_pot", new BERDecoratedPotRenderer());
        registerByKey("campfire", new BERCampfireRenderer());
        registerByKey("soul_campfire", new BERCampfireRenderer());
        registerByKey("lectern", new BERLecternRenderer());
        registerByKey("shelf", new BERShelfRenderer());
        registerByKey("copper_golem_statue", new BERCopperGolemStatueRenderer());
    }

    private static void registerByKey(String key, IInstancedRenderer<?> renderer) {
        Identifier id = Identifier.withDefaultNamespace(key);
        BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(id).ifPresent(type -> REGISTRY.put(type, renderer));
    }

    private BERRendererRegistry() {}

    public static <S extends BlockEntityRenderState> void register(
            BlockEntityType<?> type, IInstancedRenderer<S> renderer) {
        REGISTRY.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <S extends BlockEntityRenderState> IInstancedRenderer<S> getByType(BlockEntityType<?> type) {
        return (IInstancedRenderer<S>) REGISTRY.get(type);
    }
}
