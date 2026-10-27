# Armor Hide Forge

隐藏玩家身上的盔甲，鞘翅、披风、手持物品不受影响。服务端装了全服生效，客户端装了能隐藏自己的。
Hide player armor on both sides. Elytra, capes and held items are unaffected.

> 这是 [SmilingNight/armor-hide](https://github.com/SmilingNight/armor-hide)（Minecraft 26.3 / Fabric 版）的 **Forge 1.20.1 移植版**。
> This is a **Forge 1.20.1 port** of the Fabric mod [SmilingNight/armor-hide](https://github.com/SmilingNight/armor-hide).

---

## 中文

### 它做什么

装上以后，玩家之间互相看不到对方穿的头盔、胸甲、护腿、靴子。

- **装了服务端**：所有玩家互相看不见别人盔甲
- **只装客户端**：只有你自己看不见自己身上的盔甲，别人还是看得到
- **装两端**：两者都有

**只改客户端收到的那份装备数据，不碰服务端真实的装备。** 所以护甲值、附魔、耐久消耗、死亡掉落、铁砧修复全都照常。

| 视角 | 仅服务端 | 服务端 + 客户端 |
|---|---|---|
| 看别人的盔甲 | 隐藏 | 隐藏 |
| 看自己的盔甲 | 正常 | 隐藏 |
| 鞘翅 / 披风 | 可见 | 可见 |
| 手持 / 副手物品 | 正常 | 正常 |
| 背包界面 / 装备功能 | 不受影响 | 不受影响 |

### 保留的东西

| 保留 | 说明 |
|---|---|
| 鞘翅 | 胸槽里是鞘翅时不隐藏，翅膀正常显示 |
| 披风 | 披风不属于装备槽，本来就不受影响 |
| 主手 / 副手 | 手持物品正常显示 |
| 护甲属性 | 减伤、附魔效果全部生效 |

### 环境要求

| 项目 | 版本 |
|---|---|
| Minecraft | 1.20.1 |
| Forge | 47.x（开发用 47.2.0，运行时 47.4.6 已验证） |
| Java | 17 |
| 依赖 | 无 |

### 安装

1. **服务端**（全服生效）：把 `armorhide-<version>.jar` 放进 `mods/`
2. **客户端**（隐藏自己的盔甲）：同一个 jar 放进客户端 `mods/`
3. 同一个 jar 两端通用，Mixin 会按环境自动分流加载

### 实现方式（以及和原作的关系）

原作是 Minecraft 26.3 + Fabric，1.20.1 上的类名和方法名都不一样，所以三个注入点全部换过：

| 作用 | 原作（26.3 / Fabric） | 本移植版（1.20.1 / Forge） |
|---|---|---|
| 初始装备包 | `ServerEntity#sendPairingData` | `ServerEntity#sendPairingData`（同名） |
| 装备变化包 | `LivingEntity#updatePlayersWithNewEquipment` | `LivingEntity#handleEquipmentChanges`（1.20.1 没有前者） |
| 自己看不见自己 | `HumanoidMobRenderer#extractHumanoidRenderState` | `HumanoidArmorLayer#renderArmorPiece`（1.20.1 没有 render state 机制） |

两处服务端注入都是 `@ModifyArg`，只改**发给客户端的那个 `ClientboundSetEquipmentPacket`** 里的列表参数，服务端实体上的装备数据一个字节都不动。

---

## English

### What it does

Hides helmet, chestplate, leggings and boots on players.

- **Server-side install**: no player can see anyone's armor
- **Client-only install**: you can't see your own armor, others still see yours

It only filters the equipment snapshot sent to clients. The real equipment on the server is untouched, so armor points, enchantments, durability and drops all work normally.

### Kept intact

Elytra (chest slot when the item is an elytra), capes, main/off hand items, and all armor stats.

### Known side effects

- Carved pumpkin no longer blocks your view (the vignette is drawn client-side from the helmet slot)
- Turtle shell water breathing still works (server-side attribute)
- Third-party HUDs/renders that read player equipment data may misbehave

### Requirements

Minecraft 1.20.1, Forge 47.x, Java 17, no dependencies.

### Building

```bash
./gradlew build
```

Output: `build/libs/armorhide-<version>.jar`

### Differences from the original

The original targets Minecraft 26.3 / Fabric, where the injection points differ. All three hooks were re-targeted for 1.20.1 (Forge), see the table in the Chinese section above.

---

## License / 许可

MIT. See [LICENSE](LICENSE).

原项目 / Original work: [SmilingNight/armor-hide](https://github.com/SmilingNight/armor-hide) — MIT, Copyright (c) 2026 SmilingNight.
本移植版 / This port: Copyright (c) 2026 vince.
