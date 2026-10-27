package local.armorhide;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 装备列表过滤工具：把「头 / 腿 / 脚」以及「非鞘翅的胸甲」从装备列表里去掉。
 * 只动网络包里那份快照，不碰实体真实装备。
 */
public final class ArmorHideUtil {

    private ArmorHideUtil() {
    }

    public static List<Pair<EquipmentSlot, ItemStack>> filterArmor(List<Pair<EquipmentSlot, ItemStack>> slots) {
        List<Pair<EquipmentSlot, ItemStack>> out = new ArrayList<>(slots.size());
        for (Pair<EquipmentSlot, ItemStack> pair : slots) {
            EquipmentSlot slot = pair.getFirst();
            ItemStack stack = pair.getSecond();

            if (slot == EquipmentSlot.HEAD) {
                continue;
            }
            if (slot == EquipmentSlot.LEGS) {
                continue;
            }
            if (slot == EquipmentSlot.FEET) {
                continue;
            }
            // 胸槽只有在不是鞘翅的时候才隐藏；鞘翅要留着（不然翅膀没了）
            if (slot == EquipmentSlot.CHEST && !stack.is(Items.ELYTRA)) {
                continue;
            }

            out.add(pair);
        }
        return out;
    }

    /**
     * 和 {@link #filterArmor} 一样，但如果过滤后是空列表，返回一条「把原列表第一个槽位清空」的条目。
     *
     * 为什么不能直接返回空列表：ClientboundSetEquipmentPacket.write() 是「按列表长度循环写槽位」，
     * 列表为空时只写出实体 id，一个槽位字节都不写；而客户端读包是先 do-while 读一次槽位字节，
     * 于是越界（readerIndex + length exceeds writerIndex）导致解码失败、直接断线（ConnectionLostScreen）。
     * 用一条 EMPTY 占位：包结构合法，客户端还会顺手把这个槽清成空，看起来正好是「没有盔甲」。
     */
    public static List<Pair<EquipmentSlot, ItemStack>> filterArmorSafe(List<Pair<EquipmentSlot, ItemStack>> slots) {
        List<Pair<EquipmentSlot, ItemStack>> filtered = filterArmor(slots);
        if (!filtered.isEmpty() || slots.isEmpty()) {
            return filtered;
        }
        List<Pair<EquipmentSlot, ItemStack>> safe = new ArrayList<>(1);
        safe.add(Pair.of(slots.get(0).getFirst(), ItemStack.EMPTY));
        return safe;
    }
}
