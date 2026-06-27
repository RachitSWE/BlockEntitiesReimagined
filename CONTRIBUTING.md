# Contributing to Block Entities Reimagined (BER)

First off, thank you for considering contributing to Block Entities Reimagined! This mod is built to be a robust, zero-allocation, thread-safe rendering engine for Minecraft block entities. To maintain the extremely high performance and stability standards of this clean-room implementation, we have strict architectural and stylistic guidelines that all contributors must follow.

By contributing to this project, you agree to release your contributions under the **ARR (All Rights Reserved)** license terms defined by the project owner.

---

## 1. Core Development Philosophy

Before you write any code, please understand the core tenets of BER:
1. **Zero Allocations in the Hot Loop:** During the rendering phase, no new objects (e.g., `new Vector3f()`, `new BlockPos()`) should be allocated. Use pre-allocated ThreadLocal pools or mutable fields.
2. **Thread Safety:** Assume everything runs on a worker thread. Vanilla collections (`ArrayList`, `HashMap`) are strictly forbidden across thread boundaries. Use `ConcurrentHashMap` or lock-free queues.
3. **No Unbounded Tasks:** Task schedulers must use bounded queues (e.g., `DiscardOldestPolicy`) to prevent OutOfMemory crashes when the player flies through dense chunks.

### 1.1 AI Agent Concurrency & Clean-Room Compliance (Chinese Wall Protocol)

Because this mod is a clean-room implementation designed to avoid LGPLv3 viral licensing constraints of the inspiration codebase, we enforce a strict **Chinese Wall Protocol** for all human contributors and **autonomous AI coding agents** executing tasks in this repository:

1. **Reference-Only Access:** Autonomous AI coding agents are permitted to read [.gemini/all_code.txt](file:///A:/Files/Projects/Files/CubicProjects/blockentitiesreimagined/.gemini/all_code.txt) as a reference to study the underlying rendering algorithms, mixin targets, and architectural flow. However, agents are strictly forbidden from copying, translating, or reusing BBE's actual code line-by-line.
2. **Clean-Room Implementation:** AI agents must re-implement all logical paths from scratch. Code structure, variable naming, and class distributions should diverge from the original codebase to guarantee legal compliance with the All Rights Reserved license and avoid LGPLv3 viral contamination.
3. **Original Mixin Design:** While BBE's mixin strategies in `all_code.txt` can serve as references for hook locations, agents must formulate their own @Mixin implementations. Do not duplicate target descriptors, local variable captures, or fragile inject points. Re-evaluate target class bytecode for Minecraft 26.2 and Sodium 0.9.x to write robust, conflict-free injection code.
4. **Architectural Refinement:** Leverage BBE's logic for reference, but prioritize cleaner, decoupled code structures. Do not replicate anti-patterns, tight class coupling, or generic duplicates identified in [.gemini/spec.md](file:///A:/Files/Projects/Files/CubicProjects/blockentitiesreimagined/.gemini/spec.md).

---

## 2. Code Style and Conventions

We maintain a highly specific Java code style, inspired by the cleanest aspects of ecosystem standards. Your pull request will be rejected if the CI formatting checks fail.

### 2.1 Import Categorization
Imports must be grouped logically and separated by specific block comments: `/* local */`, `/* sodium */` (if applicable), and `/* minecraft */`. 

**Example Structure:**
```java
package blockentitiesreimagined.client.gui.config;

/* local */
import blockentitiesreimagined.client.BER;
import blockentitiesreimagined.client.gui.option.EnumTypes;

/* sodium */
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;

/* minecraft */
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
```

### 2.2 Builder Patterns and Chaining
When using builder patterns (especially for configs or rendering pipelines), each property mutation must reside on a new line, indented by exactly 8 spaces from the parent declaration. 

**Example of correct builder chaining:**
```java
BBERenderPage.addOptionGroup(builder.createOptionGroup()
        .addOption(
                builder.createBooleanOption(ResourceLocation.parse("ber:optimize.chest"))
                        .setName(Component.translatable("ber.config.storage.main.optimize.chest"))
                        .setTooltip(Component.translatable("ber.config.storage.main.optimize.chest.tooltip"))
                        .setDefaultValue(true)
                        .setImpact(OptionImpact.HIGH)
                        .setBinding(
                                value -> BER.GlobalScope.CONFIG.MAIN.setOption("optimize.chest", value),
                                () -> (boolean) BER.GlobalScope.CONFIG.MAIN.getOption("optimize.chest").getValue()
                        )
                        .setEnabledProvider(c ->
                                c.readBooleanOption(ResourceLocation.parse("ber:master")), ResourceLocation.parse("ber:master")
                        )
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .setStorageHandler(this.saveMainConfigStorageObject)
        )
);
```

### 2.3 Hardcoded Strings
Never hardcode user-facing strings. Always use `Component.translatable("your.translation.key")`.

### 2.4 Lambda Expressions
Keep lambda expressions concise. If a lambda requires more than 3 lines of logic, extract it into a private helper method within the class.

---

## 3. Console Logging Strategy

Maintaining clean and informative console logs is vital. We strictly follow the `GlobalScope` logging architecture used in high-performance Fabric mods. You must **never** create local `Logger` instances in your classes, and you must **never** use `System.out.println`. 

All logging must route through `BER.getLogger()`, which uses SLF4J and parameterized logging strings.

### 3.1 Logger Initialization (Internal)
The logger is initialized exactly once inside the `BER` main class using an inner `GlobalScope` class:

```java
package blockentitiesreimagined.client;

/* slf4j */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BER {
    public static class GlobalScope {
        private static final Logger LOGGER = LoggerFactory.getLogger("BER-Logger");
    }
    
    /* global logger, used for info logging, error handling, etc... */
    public static Logger getLogger() {
        return GlobalScope.LOGGER;
    }
}
```

### 3.2 Usage Across the Codebase
Whenever you need to log an event, access it statically. Always use `{}` placeholders for variables rather than string concatenation, as SLF4J optimizes this.

**Incorrect Logging:**
```java
public class MyRenderer {
    // DO NOT DO THIS
    private static final Logger LOGGER = LoggerFactory.getLogger("MyRenderer");
    
    public void run() {
        System.out.println("Starting render..."); // DO NOT DO THIS
        LOGGER.info("Pos: " + pos.toString()); // STRING CONCATENATION IS SLOW
    }
}
```

**Correct Logging:**
```java
package blockentitiesreimagined.client.render;

/* local */
import blockentitiesreimagined.client.BER;

public class MyRenderer {
    public void run() {
        // Correct Info usage
        BER.getLogger().info("Checking for loaded mods for compact...");
        
        // Correct Error usage with SLF4J placeholders
        BER.getLogger().error("Failed to fetch block entity at {}. " + 
                              "LevelSlice#getBlockEntity threw an exception!", pos, exception);
    }
}
```

---

## 4. Pull Request Process

1. **Open an Issue:** Before spending hours writing code, open an issue detailing the block entity you want to optimize or the bug you want to fix.
2. **Branching:** Create a branch from `main` named `feature/entity-name` or `fix/issue-number`.
3. **Commit Messages:** Use conventional commits.
4. **Testing:** 
   - Spawn 10,000 instances of the block entity you modified in a single chunk.
   - Run the game with `-Dber.debug=true`.
   - Take a screenshot of the F3 debug menu proving that FPS remains stable.
5. **Review:** A maintainer will review your code strictly against the code style and memory allocation rules.

---

## 5. Development Environment Setup

We recommend IntelliJ IDEA for developing BER.

1. Clone the repository.
2. Run `./gradlew genSources` to map the Minecraft source code.
3. Run `./gradlew idea` if using an older version of IntelliJ.
4. Set up IDE run configurations.

### 5.1 Required IDE Plugins
- **Minecraft Development:** Essential for Mixin validation.
- **CheckStyle:** Catch import grouping errors before committing.

---

## 6. Architectural Guide (Brief)

If you are writing a new optimizer for a block entity, you must implement the `IInstancedRenderer` interface.

```java
package blockentitiesreimagined.client.render;

/* local */
import blockentitiesreimagined.client.api.IInstancedRenderer;

/* minecraft */
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class CustomEntityRenderer<T extends BlockEntity> implements IInstancedRenderer<T> {
    
    @Override
    public void render(@NotNull T entity, float tickDelta, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers) {
        // Your zero-allocation logic here.
        // DO NOT instantiate objects in this method!
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
```
All method parameters that interact with vanilla APIs must be explicitly annotated with `@NotNull` or `@Nullable`.

---
Thank you for helping us make Minecraft faster, the right way.
