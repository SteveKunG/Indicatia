package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigOptionButtonWidget extends Button
{
    public ConfigOptionButtonWidget(int x, int y, int width, int height, String display, Button.IPressable action)
    {
        super(x, y, width, height, display, action);
    }
}