package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BERSignRendererTest {
    
    @Test
    void testIsStatic() {
        BERSignRenderer renderer = new BERSignRenderer();
        Assertions.assertTrue(renderer.isStatic(), "SignRenderer should be static.");
    }
}
