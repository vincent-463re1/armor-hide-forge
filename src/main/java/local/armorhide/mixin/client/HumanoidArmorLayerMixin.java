package local.armorhide.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * 客户端可选行为：自己也看不见自己身上的盔甲。
 * 挂在 renderArmorPiece 上（逐个槽位渲染盔甲的私有方法），
 * 只在「渲染对象就是本地玩家」时取消掉。鞘翅走 ElytraLayer，不在这里，所以翅膀还在。
 * 之所以不挂 render：1.20.1 的 HumanoidArmorLayer.render 有一个泛型版 + 一个桥接版同名方法，选择器会歧义。
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void armorhide$hideOwnArmor(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity,
                                        EquipmentSlot slot, int packedLight, HumanoidModel<?> model,
                                        CallbackInfo ci) {
        if (entity == Minecraft.getInstance().player) {
            ci.cancel();
        }
    }
}
