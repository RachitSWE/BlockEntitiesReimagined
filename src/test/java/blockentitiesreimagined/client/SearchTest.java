package blockentitiesreimagined.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SearchTest {
    @Test
    public void dumpMappings() {
        System.out.println("--- CHECKING CLASSES ---");
        String[] possibleNames = {
            "net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity",
            "net.minecraft.world.level.block.entity.UnchiseledBookShelfBlockEntity",
            "net.minecraft.world.level.block.entity.ShelfBlockEntity",
            "net.minecraft.world.level.block.entity.BookShelfBlockEntity",
            "net.minecraft.world.level.block.entity.CampfireBlockEntity"
        };
        for (String name : possibleNames) {
            try {
                Class.forName(name, false, this.getClass().getClassLoader());
                System.out.println("FOUND: " + name);
            } catch (ClassNotFoundException e) {
                System.out.println("MISSING: " + name);
            }
        }
        System.out.println("--- END CHECK ---");
    }
}
