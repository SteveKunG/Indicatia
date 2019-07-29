package stevekung.mods.indicatia.handler;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class KeyBindingHandler
{
    public static FabricKeyBinding CUSTOM_CAPE;
    public static FabricKeyBinding CONFIG;
    public static FabricKeyBinding TOGGLE_SPRINT;
    public static FabricKeyBinding TOGGLE_SNEAK;
    public static FabricKeyBinding WEATHER;

    public static void init()
    {
        KeyBindingRegistryImpl.INSTANCE.addCategory(IndicatiaMod.MOD_ID);

        KeyBindingHandler.CUSTOM_CAPE = FabricKeyBinding.Builder.create(new Identifier("indicatia:custom_cape_gui.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, IndicatiaMod.MOD_ID).build();
        KeyBindingHandler.CONFIG = FabricKeyBinding.Builder.create(new Identifier("indicatia:quick_config.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, IndicatiaMod.MOD_ID).build();
        KeyBindingHandler.TOGGLE_SPRINT = FabricKeyBinding.Builder.create(new Identifier("indicatia:toggle_sprint.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, IndicatiaMod.MOD_ID).build();
        KeyBindingHandler.TOGGLE_SNEAK = FabricKeyBinding.Builder.create(new Identifier("indicatia:toggle_sneak.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, IndicatiaMod.MOD_ID).build();
        KeyBindingHandler.WEATHER = FabricKeyBinding.Builder.create(new Identifier("indicatia:weather.desc"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, IndicatiaMod.MOD_ID).build();

        KeyBindingRegistryImpl.INSTANCE.register(KeyBindingHandler.CUSTOM_CAPE);
        KeyBindingRegistryImpl.INSTANCE.register(KeyBindingHandler.CONFIG);
        KeyBindingRegistryImpl.INSTANCE.register(KeyBindingHandler.TOGGLE_SPRINT);
        KeyBindingRegistryImpl.INSTANCE.register(KeyBindingHandler.TOGGLE_SNEAK);
        KeyBindingRegistryImpl.INSTANCE.register(KeyBindingHandler.WEATHER);
    }
}