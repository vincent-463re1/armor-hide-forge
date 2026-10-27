package local.armorhide;

import net.minecraftforge.fml.common.Mod;

/**
 * 空壳主类。这个 mod 的全部功能都由 mixin 完成，这里只负责让 FML 认领 modid。
 */
@Mod(ArmorHide.MODID)
public class ArmorHide {

    public static final String MODID = "armorhide";

    public ArmorHide() {
        // 无配置、无注册、无网络通道：装上即生效
    }
}
