package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BERShulkerRendererTest {
    
    @Test
    void testIsStatic() {
        BERShulkerRenderer renderer = new BERShulkerRenderer();
        Assertions.assertFalse(renderer.isStatic(), "ShulkerRenderer should NOT be static because it relies on dynamic opening/closing animations.");
    }
}
