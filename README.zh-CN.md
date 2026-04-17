# 现代工业化多槽流体仓

[English](./README.md)

这是一个 [**现代工业化(Modern Industrialization)**](https://github.com/AztechMC/Modern-Industrialization) 附属模组，增加了可配置的**多槽流体输入/输出仓**。

## 下载

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/rRRy353k?style=for-the-badge&logo=modrinth&label=modrinth)](https://modrinth.com/mod/mimsf)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1516477?style=for-the-badge&logo=curseforge&label=curseforge)](https://www.curseforge.com/minecraft/mc-mods/modernindustrialization-multi-slot-fluid-hatch/files/)

## 功能

- 为现代工业化添加多槽**流体输入仓**和**流体输出仓**。
- 支持 **KubeJS** 扩展注册，自定义多槽流体仓。

## 前置需求

- **Modern Industrialization**：2.4.0+

## KubeJS 支持

本模组扩展了 Modern Industrialization 的 KubeJS 仓室注册事件，可以直接创建自定义多槽流体仓。

```js
// startup_scripts/*.js
MIMachineEvents.registerHatches(event => {
    // 创建单槽流体输入/输出仓
    event.fluid("englishPrefix", "prefix", "casing", bucketCapacity);

    // 创建多槽流体输入/输出仓，槽位数量为 slotCount。
    event.fluid("englishPrefix", "prefix", "casing", bucketCapacity, slotCount);

    // 创建多槽流体输入/输出仓，每行有 rowSlotCount 个槽位，共 columSlotCount 行。
    event.fluid("englishPrefix", "prefix", "casing", bucketCapacity, rowSlotCount, columSlotCount);
})
```

## 许可证

本项目采用 **MIT License**。
