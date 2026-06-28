package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BERChestRendererTest {
    
    @Test
    void testIsStatic() {
        BERChestRenderer renderer = new BERChestRenderer();
        Assertions.assertFalse(renderer.isStatic(), "ChestRenderer should NOT be static because it relies on dynamic opening/closing animations.");
    }
}
