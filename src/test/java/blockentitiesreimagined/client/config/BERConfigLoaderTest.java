package blockentitiesreimagined.client.config;

/* java */
import java.io.File;
import java.util.concurrent.ExecutionException;

/* junit */
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BERConfigLoaderTest {
    
    @AfterEach
    public void cleanup() {
        new File("config/blockentitiesreimagined.json").delete();
    }

    @Test
    public void testConfigSaveAndLoad() throws ExecutionException, InterruptedException {
        BERConfigLoader.loadAsync().get();
        Assertions.assertNotNull(BERConfigLoader.get());
        Assertions.assertTrue(BERConfigLoader.get().master.enableOptimization);
        
        BERConfigLoader.get().culling.signTextRenderDistance = 128;
        BERConfigLoader.saveAsync().get();
        
        BERConfigLoader.loadAsync().get();
        Assertions.assertEquals(128, BERConfigLoader.get().culling.signTextRenderDistance);
    }
}
