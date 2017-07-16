package stevekung.mods.indicatia.handler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBindingIU extends KeyBinding
{
    public KeyBindingIU(String description, KeyModifier keyModifier, int keyCode)
    {
        super(description, new KeyConflictContextHandler(), keyModifier, keyCode, "key.indicatia.category");
    }
}