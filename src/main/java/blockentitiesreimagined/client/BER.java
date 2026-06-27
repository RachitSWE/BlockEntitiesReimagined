package blockentitiesreimagined.client;

/* fabric */
import net.fabricmc.api.ClientModInitializer;

/* slf4j */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BER implements ClientModInitializer {
    public static final String MOD_ID = "blockentitiesreimagined";

    private static class GlobalScope {
        private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    }

    public static Logger getLogger() {
        return GlobalScope.LOGGER;
    }

    @Override
    public void onInitializeClient() {
        getLogger().info("Initializing Block Entities Reimagined optimization layer...");
    }
}
