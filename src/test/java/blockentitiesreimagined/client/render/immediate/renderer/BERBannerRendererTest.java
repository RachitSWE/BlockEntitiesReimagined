package blockentitiesreimagined.client.render.immediate.renderer;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BERBannerRendererTest {
    
    @Test
    void testIsStatic() {
        BERBannerRenderer renderer = new BERBannerRenderer();
        Assertions.assertTrue(renderer.isStatic(), "BannerRenderer should be static according to PRD.");
    }
}
