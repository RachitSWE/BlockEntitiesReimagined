package blockentitiesreimagined.client;

import net.fabricmc.api.ClientModInitializer;
import blockentitiesreimagined.client.config.BERConfigLoader;
import blockentitiesreimagined.client.api.IRenderBackend;
import blockentitiesreimagined.client.render.instanced.BERStaticEntityInstancer;

public class BlockEntitiesReimaginedClient implements ClientModInitializer {
	private static IRenderBackend backend;
	private static BERStaticEntityInstancer instancer;

	public static IRenderBackend getBackend() {
		return backend;
	}

	public static BERStaticEntityInstancer getInstancer() {
		return instancer;
	}

	@Override
	public void onInitializeClient() {
		if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("sodium")) {
			backend = new blockentitiesreimagined.client.render.backend.BERSodiumRenderBackend();
		} else {
			backend = new blockentitiesreimagined.client.render.backend.BERVanillaRenderBackend();
		}
		backend.init();
		instancer = new BERStaticEntityInstancer(backend, 10000);

		// Register client load/unload event handlers for zero-allocation instanced block entities
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
			net.minecraft.world.level.chunk.LevelChunk chunk = world.getChunkAt(blockEntity.getBlockPos());
			if (chunk != null) {
				instancer.addEntity(chunk, blockEntity);
			}
		});

		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
			net.minecraft.world.level.chunk.LevelChunk chunk = world.getChunkAt(blockEntity.getBlockPos());
			if (chunk != null) {
				instancer.removeEntity(chunk, blockEntity);
			}
		});

		BERConfigLoader.loadAsync().thenRun(() -> {
			BER.getLogger().info("Configuration loaded. Optimization active: {}", 
				BERConfigLoader.get().master.enableOptimization);
		});
	}
}
