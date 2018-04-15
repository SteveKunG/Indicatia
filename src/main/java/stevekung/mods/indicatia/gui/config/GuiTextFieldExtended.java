package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;

@SideOnly(Side.CLIENT)
public class GuiTextFieldExtended extends GuiTextField
{
    private final ExtendedConfig.Options options;

    public GuiTextFieldExtended(int id, int x, int y, int width, ExtendedConfig.Options options)
    {
        super(id, Minecraft.getMinecraft().fontRenderer, x, y, width, 20);
        this.options = options;
        this.setEnabled(true);
        this.setMaxStringLength(13);
    }

    public ExtendedConfig.Options getOption()
    {
        return this.options;
    }
}