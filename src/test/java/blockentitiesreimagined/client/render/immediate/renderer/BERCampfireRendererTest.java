package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* junit */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BERCampfireRendererTest {

    @Test
    public void testIsStatic() {
        BERCampfireRenderer renderer = new BERCampfireRenderer();
        assertFalse(renderer.isStatic(), "Campfire renderer must be dynamic to render items");
    }

    @Test
    public void testImplementsInterface() {
        BERCampfireRenderer renderer = new BERCampfireRenderer();
        assertTrue(renderer instanceof IInstancedRenderer, "Must implement IInstancedRenderer");
    }
}
