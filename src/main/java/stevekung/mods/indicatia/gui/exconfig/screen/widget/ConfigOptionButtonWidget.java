package stevekung.mods.indicatia.gui.exconfig.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public class ConfigOptionButtonWidget extends ButtonWidget
{
    public ConfigOptionButtonWidget(int x, int y, int width, int height, String display, ButtonWidget.PressAction action)
    {
        super(x, y, width, height, display, action);
    }
}