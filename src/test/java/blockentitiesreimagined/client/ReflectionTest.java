package blockentitiesreimagined.client;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;

public class ReflectionTest {
    @Test
    public void testMinecraftMethods() {
        System.out.println("--- Minecraft Methods ---");
        for (Method m : Minecraft.class.getDeclaredMethods()) {
            if (m.getName().toLowerCase().contains("item")) {
                System.out.println(m.getReturnType().getName() + " " + m.getName());
            }
        }
    }
}
