package blockentitiesreimagined.client;

import net.fabricmc.api.ClientModInitializer;
import blockentitiesreimagined.client.config.BERConfigLoader;

public class BlockEntitiesReimaginedClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BERConfigLoader.loadAsync().thenRun(() -> {
			BER.getLogger().info("Configuration loaded. Optimization active: {}", 
				BERConfigLoader.get().master.enableOptimization);
		});
	}
}
