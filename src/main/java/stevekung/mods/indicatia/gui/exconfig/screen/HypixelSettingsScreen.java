package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ConfigButtonListWidget;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class HypixelSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.RIGHT_CLICK_ADD_PARTY);
    }

    HypixelSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Hypixel Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.instance.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 32, this.height - 32, 25);
        this.optionsRowList.addAll(OPTIONS.toArray(new ExtendedConfigOption[OPTIONS.size()]));
        this.children.add(this.optionsRowList);
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
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.hypixel.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}