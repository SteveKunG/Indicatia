package stevekung.mods.indicatia.handler;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekungslib.keybinding.KeyBindingBase;

public class KeyBindingHandler
{
    public static KeyBinding KEY_TOGGLE_SPRINT;
    public static KeyBinding KEY_TOGGLE_SNEAK;
    public static KeyBinding KEY_REC_OVERLAY;
    public static KeyBinding KEY_CUSTOM_CAPE_GUI;
    public static KeyBinding KEY_QUICK_CONFIG;
    public static KeyBinding KEY_DONATOR_GUI;

    public static void init()
    {
        KeyBindingHandler.KEY_TOGGLE_SPRINT = new KeyBindingBase("key.toggle_sprint.desc", IndicatiaMod.MOD_ID, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_S));
        KeyBindingHandler.KEY_TOGGLE_SNEAK = new KeyBindingBase("key.toggle_sneak.desc", IndicatiaMod.MOD_ID, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_LEFT_SHIFT));
        KeyBindingHandler.KEY_REC_OVERLAY = new KeyBinding("key.rec_overlay.desc", GLFW.GLFW_KEY_UNKNOWN, "key.indicatia.category");
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyBinding("key.quick_config.desc", GLFW.GLFW_KEY_F4, "key.indicatia.category");
        KeyBindingHandler.KEY_DONATOR_GUI = new KeyBinding("key.donator_gui.desc", GLFW.GLFW_KEY_F6, "key.indicatia.category");
        KeyBindingHandler.KEY_CUSTOM_CAPE_GUI = new KeyBinding("key.custom_cape_gui.desc", GLFW.GLFW_KEY_H, "key.indicatia.category");

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_CUSTOM_CAPE_GUI);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SPRINT);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SNEAK);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_REC_OVERLAY);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_DONATOR_GUI);
    }
}