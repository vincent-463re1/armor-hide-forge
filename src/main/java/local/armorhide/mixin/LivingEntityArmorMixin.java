package local.armorhide.mixin;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.datafixers.util.Pair;

import local.armorhide.ArmorHideUtil;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 玩家换装备时，服务端会在 handleEquipmentChanges 里拼一个「变更了的槽位」列表并广播出去，
 * 在这里把盔甲槽位滤掉。
 * 注意：1.20.1 没有 updatePlayersWithNewEquipment，装备变更是走私有方法 handleEquipmentChanges(Map)。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityArmorMixin {

    @ModifyArg(
            method = "handleEquipmentChanges",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ClientboundSetEquipmentPacket;<init>(ILjava/util/List;)V"
            ),
            index = 1
    )
    private List<Pair<EquipmentSlot, ItemStack>> armorhide$hideChanged(
            List<Pair<EquipmentSlot, ItemStack>> slots) {
        return ((Object) this) instanceof Player ? ArmorHideUtil.filterArmorSafe(slots) : slots;
    }
}
