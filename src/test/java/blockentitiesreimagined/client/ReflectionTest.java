package blockentitiesreimagined.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;

public class ReflectionTest {
    @Test
    public void testMinecraftMethods() {
        System.out.println("--- CampfireRenderer Methods ---");
        for (Method m : CampfireRenderer.class.getDeclaredMethods()) {
            System.out.println(m.getReturnType().getName() + " " + m.getName());
        }
        System.out.println("--- CampfireRenderer Fields ---");
        for (Field f : CampfireRenderer.class.getDeclaredFields()) {
            System.out.println(f.getType().getName() + " " + f.getName());
        }
    }
}
