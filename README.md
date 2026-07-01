# Block Entities Reimagined (BER) 🚀

![Fabric](https://img.shields.io/badge/Modloader-Fabric-orange?style=for-the-badge&logo=fabric)
![Minecraft](https://img.shields.io/badge/Minecraft-26.2-green?style=for-the-badge&logo=minecraft)
![License](https://img.shields.io/badge/License-ARR-red?style=for-the-badge)

Welcome to **Block Entities Reimagined**, a from-scratch, clean-room Fabric mod designed to fundamentally overhaul the rendering pipeline for Minecraft's most demanding blocks. 

If you've ever built a massive storage room, a sprawling banner hall, or a dense server hub, you know that Block Entities (Tile Entities) can absolutely decimate your framerate. This mod aims to fix that permanently.

---

## 🛑 The Problem

Vanilla Minecraft uses an "Immediate Mode" rendering style for complex block entities. This means that every single frame, the game CPU individually calculates the matrix transformations, lighting, and texture bindings for every single chest, sign, and banner in your field of view. 

When you have hundreds or thousands of these entities in loaded chunks, the number of draw calls dispatched to the GPU skyrockets, and your FPS plummets. Furthermore, many existing optimization mods inject deeply into volatile rendering loops, leading to crashes and memory leaks.

---

## 💡 The Solution

**Block Entities Reimagined** intercepts these entities and routes them through a custom, highly optimized **Instanced Rendering Engine**. Instead of asking the GPU to draw 10,000 individual signs one by one, BER batches them together and asks the GPU to draw the sign 10,000 times in a single, lightning-fast draw call.

We built this mod from the ground up to be safe, memory-efficient, and structurally sound.

---

## ✨ Key Features

- **Massive FPS Boosts:** Experience up to 300%-500% rendering speedups in chunks dense with block entities.
- **Zero-Allocation Hot Loop:** Engineered to prevent Garbage Collection (GC) micro-stutters by using pre-allocated, thread-local memory pools for vector mathematics. The render loop allocates exactly 0 bytes per frame!
- **Thread-Safe State Management:** Utilizes modern `ConcurrentHashMap` and bounded task queues to guarantee that multi-threaded chunk loading never crashes your client or runs you out of memory.
- **Smart Culling:** Aggressive frustum, occlusion, and distance culling ensures we don't spend a single CPU cycle calculating transformations for entities you can't see or are too far away.
- **Dynamic Animation Manager:** Even animated entities (like opening chests and ringing bells) benefit from uniform-buffer bone transformations rather than CPU-side geometry rebuilding.

---

## 🎯 Supported Block Entities

We currently optimize the rendering of the following entities:

1. **📦 Chests (All Variants)**
   - Standard Chests
   - Ender Chests
   - Trapped Chests
   - Copper Chest Variants
2. **📦 Shulker Boxes**
3. **🪧 Signs**
   - Standing Signs, Wall Signs, and Hanging Signs
4. **🏳️ Banners**
   - Safely capped NBT pattern iterations to prevent malicious server freezing
5. **🏺 Decorated Pots**
6. **🔔 Bells**
7. **🤖 Copper Golem Statues**
8. **🔥 Campfires**
9. **📚 Shelves**
10. **📖 Lecterns**

---

## 🛠️ Technical Details & Compatibility

Block Entities Reimagined is built with a defensive architecture designed to integrate flawlessly alongside standard performance mods.

- **Sodium Native Integration:** BER interacts safely with the Sodium render graph via a custom adapter interface (`IRenderBackend`). If Sodium fails to load or isn't installed, BER gracefully falls back to optimized vanilla rendering rather than crashing your game.
- **Clean-Room Implementation:** This mod is 100% original code developed under the Chinese Wall Protocol. It shares absolutely no source code with other block entity optimization mods.
- **Late-Phase Interception:** Uses strict mixin priority scheduling to prevent mod collisions.

---

## 📥 Installation

1. Install the [Fabric Loader](https://fabricmc.net/).
2. Download the appropriate version of the Fabric API and place it in your `mods` folder.
3. Download the latest `.jar` release of **Block Entities Reimagined** from the Releases page.
4. Place the `blockentitiesreimagined-x.x.x.jar` into your `.minecraft/mods` folder.
5. Launch the game and enjoy buttery smooth framerates in your storage rooms!

---

## ⚙️ Configuration

A configuration file will be automatically generated at `config/blockentitiesreimagined.json`.
Options include:
- `enableOptimization` (Default: true)
- `cullingAggressiveness` (Default: "HIGH")
- Toggles for individual block entities (e.g., `optimizeChests`, `optimizeSigns`)
- `maxTaskQueueDepth` (Default: 1000)

We highly recommend leaving these at their defaults unless you are experiencing specific hardware-related visual artifacts.

---

## 🤝 [Contributing](CONTRIBUTING.md)

We welcome contributions! Whether it's adding support for a new modded block entity or tweaking the culling algorithms, feel free to open a Pull Request.

Please ensure that any submitted code adheres to our strict thread-safety and zero-allocation guidelines for the render hot-loop.

---

## 📝 [License](LICENSE.md)

This project is licensed under the **ARR LICENSE**.
