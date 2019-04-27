package stevekung.mods.indicatia.handler;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import stevekung.mods.stevekungslib.utils.client.ClientRegistryUtils;

public class KeyBindingHandler
{
    public static void init()
    {
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:custom_cape_gui.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "Indicatia");
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:donator_gui.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "Indicatia");
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:quick_config.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "Indicatia");
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:rec_overlay.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "Indicatia");
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:toggle_sprint.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "Indicatia");
        ClientRegistryUtils.registerKeyBinding(new Identifier("indicatia:toggle_sneak.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "Indicatia");
    }
}