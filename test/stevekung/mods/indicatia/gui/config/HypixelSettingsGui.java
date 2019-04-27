package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class HypixelSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigListWidget optionsRowList;
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.Options.RIGHT_CLICK_ADD_PARTY);
    }

    HypixelSettingsScreen(Screen parent)
    {
        this.parent = parent;
    }

    @Override
    public void onInitialized()
    {
        this.addButton(new ButtonWidget(200, this.width / 2 - 100, this.height - 27, LangUtils.translate("gui.done"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                ExtendedConfig.save();
                HypixelSettingsScreen.this.client.openScreen(HypixelSettingsScreen.this.parent);
            }
        });

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new ConfigListWidget(this.width, this.height, 32, this.height - 32, 25, options);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();
        this.optionsRowList.draw(mouseX, mouseY, partialTicks);
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.hypixel.title"), this.width / 2, 5, 16777215);
        super.draw(mouseX, mouseY, partialTicks);
    }
}