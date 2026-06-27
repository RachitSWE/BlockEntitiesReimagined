package blockentitiesreimagined.client.render.immediate.blockentity.manager;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/* minecraft */
import net.minecraft.core.BlockPos;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;

public class InstancedBlockEntityManagerTest {

    @BeforeAll
    public static void setup() {
        net.minecraft.SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    private static class DummyEntity extends BlockEntity {
        public DummyEntity() {
            super(BlockEntityTypes.CHEST, BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        }
    }

    @Test
    public void testLifecyclePhasesAndWeakMapping() {
        BlockEntity entity = new DummyEntity();
        InstancedBlockEntityManager manager = InstancedBlockEntityManager.getOrCreate(entity);
        
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.IDLE, manager.getPhase());
        Assertions.assertTrue(InstancedBlockEntityManager.isBaked(entity));
        
        manager.setAnimating(true);
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.IMMEDIATE_ACTIVE, manager.getPhase());
        Assertions.assertFalse(InstancedBlockEntityManager.isBaked(entity));
        
        manager.setAnimating(false);
        manager.requestRebuild();
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.WAITING_TERRAIN, manager.getPhase());
        Assertions.assertTrue(InstancedBlockEntityManager.isBaked(entity));
    }

    @Test
    public void testInterruptingRebuildWithAnimation() {
        BlockEntity entity = new DummyEntity();
        InstancedBlockEntityManager manager = InstancedBlockEntityManager.getOrCreate(entity);

        // 1. Terrain starts rebuilding
        manager.requestRebuild();
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.WAITING_TERRAIN, manager.getPhase());

        // 2. Player opens the chest (animation overrides terrain state immediately)
        manager.setAnimating(true);
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.IMMEDIATE_ACTIVE, manager.getPhase());

        // 3. Player closes the chest (should revert to WAITING_TERRAIN because rebuild hasn't finished)
        manager.setAnimating(false);
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.WAITING_TERRAIN, manager.getPhase());

        // 4. Chunk rebuild finally finishes
        manager.onRebuildComplete();
        Assertions.assertEquals(InstancedBlockEntityManager.Phase.IDLE, manager.getPhase());
    }
}
