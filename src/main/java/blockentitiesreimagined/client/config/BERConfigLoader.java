package blockentitiesreimagined.client.config;

/* gson */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/* java */
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

/* local */
import blockentitiesreimagined.client.BER;

public final class BERConfigLoader {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final File FILE = new File("config/blockentitiesreimagined.json");
    private static volatile ConfigInstance activeConfig = new ConfigInstance();

    private BERConfigLoader() {}

    public static ConfigInstance get() {
        return activeConfig;
    }

    public static CompletableFuture<Void> loadAsync() {
        return CompletableFuture.runAsync(BERConfigLoader::load);
    }

    public static CompletableFuture<Void> saveAsync() {
        return CompletableFuture.runAsync(BERConfigLoader::save);
    }

    public static synchronized void load() {
        if (!FILE.exists()) {
            saveDefault();
            return;
        }

        try (Reader reader = Files.newBufferedReader(FILE.toPath(), StandardCharsets.UTF_8)) {
            activeConfig = GSON.fromJson(reader, ConfigInstance.class);
            if (activeConfig == null) {
                activeConfig = new ConfigInstance();
            }
        } catch (Exception e) {
            BER.getLogger().error("Failed to load configuration file", e);
            activeConfig = new ConfigInstance();
        }
    }

    public static synchronized void save() {
        File parent = FILE.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (Writer writer = Files.newBufferedWriter(FILE.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(activeConfig, writer);
        } catch (Exception e) {
            BER.getLogger().error("Failed to save configuration file", e);
        }
    }

    private static void saveDefault() {
        activeConfig = new ConfigInstance();
        save();
    }

    public static class ConfigInstance {
        public Master master = new Master();
        public Culling culling = new Culling();
        public Optimization optimization = new Optimization();
        public Limits limits = new Limits();

        public static class Master {
            public boolean enableOptimization = true;
            public String cullingAggressiveness = "HIGH";
        }

        public static class Culling {
            public boolean frustumCulling = true;
            public boolean occlusionCulling = true;
            public boolean distanceCulling = true;
            public int signTextRenderDistance = 64;
        }

        public static class Optimization {
            public boolean optimizeChests = true;
            public boolean optimizeShulkers = true;
            public boolean optimizeSigns = true;
            public boolean optimizeBanners = true;
            public boolean optimizeBeds = true;
            public boolean optimizeBells = true;
            public boolean optimizeDecoratedPots = true;
            public boolean optimizeCopperGolemStatues = true;
            public boolean optimizeCampfires = true;
            public boolean optimizeShelves = true;
            public boolean optimizeLecterns = true;
        }

        public static class Limits {
            public int maxTaskQueueDepth = 1000;
            public int maxBannerNbtIterations = 6;
        }
    }
}
