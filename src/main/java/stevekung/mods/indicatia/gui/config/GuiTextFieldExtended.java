package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
class GuiTextFieldExtended extends GuiTextField
{
    private final ExtendedConfig.Options options;
    private final int id;

    GuiTextFieldExtended(int id, int x, int y, int width, ExtendedConfig.Options options)
    {
        super(id, Minecraft.getInstance().fontRenderer, x, y, width, 20);
        this.id = id;
        this.options = options;
        this.setEnabled(true);
        this.setMaxStringLength(13);
    }

    ExtendedConfig.Options getOption()
    {
        return this.options;
    }

    public int getId()
    {
        return this.id;
    }
}