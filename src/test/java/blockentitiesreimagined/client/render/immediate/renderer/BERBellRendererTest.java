package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BERBellRendererTest {
    @Test
    void testIsStatic() {
        BERBellRenderer renderer = new BERBellRenderer();
        assertFalse(renderer.isStatic(), "Bell renderer must be dynamic");
    }
}
