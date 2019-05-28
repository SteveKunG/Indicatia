package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.ExtendedConfigOption;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class GuiHypixelSettings extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.RIGHT_CLICK_ADD_PARTY);
    }

    GuiHypixelSettings(Screen parent)
    {
        super(JsonUtils.create("Hypixel Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, LangUtils.translate("gui.done"), (buttonWidget) ->
        {
            ExtendedConfig.instance.save();
            this.minecraft.openScreen(this.parent);
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 32, this.height - 32, 25);
        this.optionsRowList.addAll(OPTIONS.toArray(new ExtendedConfigOption[OPTIONS.size()]));
        this.children.add(this.optionsRowList);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent)
    {
        int i = this.minecraft.options.guiScale;

        if (super.mouseReleased(mouseX, mouseY, mouseEvent))
        {
            return true;
        }
        else if (this.optionsRowList.mouseReleased(mouseX, mouseY, mouseEvent))
        {
            if (this.minecraft.options.guiScale != i)
            {
                this.minecraft.onResolutionChanged();
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.instance.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.hypixel.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}