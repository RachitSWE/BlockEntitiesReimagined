package blockentitiesreimagined.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ReflectionTest {
    @Test
    public void testMinecraftMethods() {
        System.out.println("--- Frustum Methods ---");
        for (Method m : Frustum.class.getDeclaredMethods()) {
            StringBuilder params = new StringBuilder();
            for (Class<?> p : m.getParameterTypes()) {
                params.append(p.getSimpleName()).append(", ");
            }
            System.out.println(m.getReturnType().getSimpleName() + " " + m.getName() + "(" + params.toString() + ")");
        }
        System.out.println("--- BlockEntity Methods ---");
        for (Method m : BlockEntity.class.getDeclaredMethods()) {
            if (m.getName().toLowerCase().contains("remove")) {
                System.out.println(m.getReturnType().getSimpleName() + " " + m.getName());
            }
        }
    }
}
