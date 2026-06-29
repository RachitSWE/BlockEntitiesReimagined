package blockentitiesreimagined.client.render.immediate.renderer;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BERLecternRendererTest {

    @Test
    void testIsStatic() {
        BERLecternRenderer renderer = new BERLecternRenderer();
        Assertions.assertFalse(renderer.isStatic(), "Lectern renderer must be dynamic to render book pages turning");
    }

    @Test
    void testImplementsInterface() {
        BERLecternRenderer renderer = new BERLecternRenderer();
        Assertions.assertTrue(renderer instanceof IInstancedRenderer, "Must implement IInstancedRenderer");
    }
}
