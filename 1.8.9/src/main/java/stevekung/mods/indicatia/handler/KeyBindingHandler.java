package stevekung.mods.indicatia.handler;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import stevekung.mods.indicatia.config.ConfigManager;

public class KeyBindingHandler
{
    public static KeyBinding KEY_REC_COMMAND;
    public static KeyBinding KEY_CUSTOM_CAPE_GUI;
    public static KeyBinding KEY_QUICK_CONFIG;
    public static KeyBinding KEY_DONATOR_GUI;

    public static void init()
    {
        KeyBindingHandler.KEY_REC_COMMAND = new KeyBindingIU("key.rec_overlay.desc", Keyboard.KEY_F9);
        KeyBindingHandler.KEY_QUICK_CONFIG = new KeyBindingIU("key.quick_config.desc", Keyboard.KEY_F4);
        KeyBindingHandler.KEY_DONATOR_GUI = new KeyBindingIU("key.donator_gui.desc", Keyboard.KEY_F6);

        if (ConfigManager.enableCustomCape)
        {
            KeyBindingHandler.KEY_CUSTOM_CAPE_GUI = new KeyBindingIU("key.custom_cape_gui.desc", Keyboard.KEY_H);
            ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_CUSTOM_CAPE_GUI);
        }

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_REC_COMMAND);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_QUICK_CONFIG);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_DONATOR_GUI);
    }
}