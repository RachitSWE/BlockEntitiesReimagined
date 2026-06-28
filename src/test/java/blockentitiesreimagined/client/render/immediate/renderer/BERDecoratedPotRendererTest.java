package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BERDecoratedPotRendererTest {
    @Test
    void testIsStatic() {
        BERDecoratedPotRenderer renderer = new BERDecoratedPotRenderer();
        assertTrue(renderer.isStatic(), "Decorated Pot renderer must be static");
    }
}
