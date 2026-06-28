package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* junit */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BERShelfRendererTest {

    @Test
    public void testIsStatic() {
        BERShelfRenderer renderer = new BERShelfRenderer();
        assertFalse(renderer.isStatic(), "Shelf renderer must be dynamic to render items");
    }

    @Test
    public void testImplementsInterface() {
        BERShelfRenderer renderer = new BERShelfRenderer();
        assertTrue(renderer instanceof IInstancedRenderer, "Must implement IInstancedRenderer");
    }
}
