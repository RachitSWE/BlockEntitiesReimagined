package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* junit */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BERCopperGolemStatueRendererTest {

    @Test
    public void testIsStatic() {
        BERCopperGolemStatueRenderer renderer = new BERCopperGolemStatueRenderer();
        assertTrue(renderer.isStatic(), "Copper Golem Statue renderer must be static for chunk baking");
    }

    @Test
    public void testImplementsInterface() {
        BERCopperGolemStatueRenderer renderer = new BERCopperGolemStatueRenderer();
        assertTrue(renderer instanceof IInstancedRenderer, "Must implement IInstancedRenderer");
    }
}
