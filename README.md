# Block Entities Reimagined (BER) 🚀

![Fabric](https://img.shields.io/badge/Modloader-Fabric-orange?style=for-the-badge&logo=fabric)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20+-green?style=for-the-badge&logo=minecraft)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

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

- **Zero-Allocation Hot Loop:** Engineered to prevent Garbage Collection (GC) micro-stutters by using pre-allocated, thread-local memory pools for vector mathematics.
- **Thread-Safe State Management:** Utilizes modern `ConcurrentHashMap` and bounded task queues (`ThreadPoolExecutor.DiscardOldestPolicy`) to guarantee that multi-threaded chunk loading (like Sodium) never crashes your client or runs you out of memory.
- **Advanced Culling:** Aggressive frustum and occlusion culling ensures we don't spend a single CPU cycle calculating transformations for entities you can't even see.
- **Dynamic Animation Manager:** Even animated entities (like opening chests and ringing bells) benefit from uniform-buffer bone transformations rather than CPU-side geometry rebuilding.

---

## 🎯 Supported Block Entities

We currently optimize the rendering of the following entities:

1. **📦 Chests (All Variants)**
   - Standard Chests
   - Ender Chests
   - Trapped Chests
   - *New!* Copper Chest Variants
2. **🪧 Signs**
   - Standing Signs, Wall Signs, and Hanging Signs
3. **🏳️ Banners**
   - Safely capped NBT pattern iterations to prevent malicious server freezing
4. **🏺 Decorated Pots**
5. **🛏️ Beds**
6. **🔔 Bells**
7. **📦 Shulker Boxes**
8. **🤖 Copper Golem Statues**
9. **📖 Lecterns**

---

## 🛠️ Technical Details & Compatibility

Block Entities Reimagined is built with a defensive architecture designed to integrate flawlessly alongside standard performance mods.

- **Sodium Compatibility:** BER interacts safely with the Sodium render graph. If Sodium's API changes or fails to load, BER gracefully falls back to optimized vanilla rendering rather than crashing your game.
- **Clean-Room Implementation:** This mod is 100% original code. It shares absolutely no source code with other block entity optimization mods, avoiding any GPL/LGPL viral licensing constraints.
- **Performance Telemetry:** (Optional) Use JVM flag `-Dber.debug=true` to enable an F3 overlay that tracks batched draw calls, culled entities, and task queue depth.

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
- `enableInstancedRendering` (Default: true)
- `cullingAggressiveness` (Default: "HIGH")
- `maxTaskQueueDepth` (Default: 1000)

We highly recommend leaving these at their defaults unless you are experiencing specific hardware-related visual artifacts.

---

## 🤝 Contributing

We welcome contributions! Whether it's adding support for a new modded block entity or tweaking the culling algorithms, feel free to open a Pull Request.

Please ensure that any submitted code adheres to our strict thread-safety and zero-allocation guidelines for the render hot-loop.

---

## 📝 License

This project is licensed under the **ARR LICENSE**. 