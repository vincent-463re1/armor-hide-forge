package local.armorhide.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.datafixers.util.Pair;

import local.armorhide.ArmorHideUtil;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 玩家刚进入视野时的「首次配对数据」里带了一份装备列表，在这里过滤掉盔甲。
 */
@Mixin(ServerEntity.class)
public abstract class ServerEntityArmorMixin {

    @Shadow
    @Final
    private Entity f_8510_;

    @ModifyArg(
            method = "sendPairingData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ClientboundSetEquipmentPacket;<init>(ILjava/util/List;)V"
            ),
            index = 1
    )
    private List<Pair<EquipmentSlot, ItemStack>> armorhide$hideInitial(
            List<Pair<EquipmentSlot, ItemStack>> slots) {
        return this.f_8510_ instanceof Player ? ArmorHideUtil.filterArmorSafe(slots) : slots;
    }
}
