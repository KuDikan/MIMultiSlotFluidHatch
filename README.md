# ModernIndustrialization Multi-Slot Fluid Hatch

[简体中文](./README.zh-CN.md)

An addon for [**Modern Industrialization**](https://github.com/AztechMC/Modern-Industrialization) that adds configurable **multi-slot fluid input/output hatches**.

## Downloads

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/rRRy353k?style=for-the-badge&logo=modrinth&label=modrinth)](https://modrinth.com/mod/mimsf)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1516477?style=for-the-badge&logo=curseforge&label=curseforge)](https://www.curseforge.com/minecraft/mc-mods/modernindustrialization-multi-slot-fluid-hatch/files/)

## Features

- Adds multi-slot **fluid input** and **fluid output** hatches to Modern Industrialization.

- Supports **KubeJS** hatch registration extensions for custom multi-slot fluid hatches.

## Requirements

- **Modern Industrialization**: 2.4.0+

## KubeJS Support

This mod extends the Modern Industrialization KubeJS fluid hatch registration event so you can create custom multi-slot fluid hatches.

```js
// startup_scripts/*.js
MIMachineEvents.registerHatches(event => {
  // Create single-slot fluid input/output hatch
  event.fluid("englishPrefix", "prefix", "casing", bucketCapacity);

  // Create multi-slot fluid input/output hatch, total slot count is slotCount
  event.fluid("englishPrefix", "prefix", "casing", bucketCapacity, slotCount);

  //  Create multi-slot fluid input/output hatch, with rowSlotCount slots per row and columSlotCount rows
  event.fluid("englishPrefix", "prefix", "casing", bucketCapacity, rowSlotCount, columSlotCount);
})
```

## License

This project is licensed under the **MIT License**.
