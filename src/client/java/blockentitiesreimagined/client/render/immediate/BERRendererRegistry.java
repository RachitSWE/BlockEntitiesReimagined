package blockentitiesreimagined.client.render.immediate;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERBannerRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERBellRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERCampfireRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERChestRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERCopperGolemStatueRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERDecoratedPotRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERLecternRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERShelfRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERShulkerRenderer;
import blockentitiesreimagined.client.render.immediate.renderer.BERSignRenderer;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

/* java */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public final class BERRendererRegistry {
    private static final Map<Class<? extends BlockEntity>, IInstancedRenderer<?>> REGISTRY = new ConcurrentHashMap<>();

    static {
        BERChestRenderer chestRenderer = new BERChestRenderer();
        register(ChestBlockEntity.class, chestRenderer);
        register(EnderChestBlockEntity.class, chestRenderer);
        register(net.minecraft.world.level.block.entity.TrappedChestBlockEntity.class, chestRenderer);
        
        register(ShulkerBoxBlockEntity.class, new BERShulkerRenderer());
        register(SignBlockEntity.class, new BERSignRenderer());
        register(HangingSignBlockEntity.class, new BERSignRenderer());
        register(BannerBlockEntity.class, new BERBannerRenderer());
        register(BellBlockEntity.class, new BERBellRenderer());
        register(DecoratedPotBlockEntity.class, new BERDecoratedPotRenderer());
        register(CampfireBlockEntity.class, new BERCampfireRenderer());
        register(LecternBlockEntity.class, new BERLecternRenderer());
        
        try {
            Class<?> copperChest = Class.forName("net.minecraft.world.level.block.entity.CopperChestBlockEntity");
            if (BlockEntity.class.isAssignableFrom(copperChest)) {
                REGISTRY.put((Class<? extends BlockEntity>) copperChest, chestRenderer);
            }
        } catch (ClassNotFoundException ignored) {}

        try {
            Class<?> copperGolem = Class.forName("net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity");
            if (BlockEntity.class.isAssignableFrom(copperGolem)) {
                REGISTRY.put((Class<? extends BlockEntity>) copperGolem, new BERCopperGolemStatueRenderer());
            }
        } catch (ClassNotFoundException ignored) {}
        
        try {
            Class<?> shelfClass = Class.forName("net.minecraft.world.level.block.entity.ShelfBlockEntity");
            if (BlockEntity.class.isAssignableFrom(shelfClass)) {
                REGISTRY.put((Class<? extends BlockEntity>) shelfClass, new BERShelfRenderer());
            }
        } catch (ClassNotFoundException ignored) {}
    }

    private BERRendererRegistry() {}

    public static <T extends BlockEntity> void register(Class<T> type, IInstancedRenderer<? super T> renderer) {
        REGISTRY.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> IInstancedRenderer<T> get(T entity) {
        return (IInstancedRenderer<T>) REGISTRY.get(entity.getClass());
    }
}
