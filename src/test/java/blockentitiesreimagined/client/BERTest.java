package blockentitiesreimagined.client;

/* junit */
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BERTest {
    @Test
    public void testLoggerInitialization() {
        Assertions.assertNotNull(BER.getLogger());
        Assertions.assertEquals(BER.MOD_ID, BER.getLogger().getName());
    }
}
