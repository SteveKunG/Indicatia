package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class OffsetSettingsGui extends Gui
{
    private final Gui parent;
    private ConfigListWidget optionsRowList;
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.Options.ARMOR_HUD_Y);
        OPTIONS.add(ExtendedConfig.Options.POTION_HUD_Y);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_Y);
        OPTIONS.add(ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY);
        OPTIONS.add(ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET);
        OPTIONS.add(ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP);
    }

    OffsetSettingsGui(Gui parent)
    {
        this.parent = parent;
    }

    @Override
    public void onInitialized()
    {
        this.addButton(new ButtonWidget(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                ExtendedConfig.save();
                OffsetSettingsGui.this.client.openGui(OffsetSettingsGui.this.parent);
            }
        });
        this.addButton(new ButtonWidget(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("message.preview"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                ExtendedConfig.save();
                OffsetSettingsGui.this.client.openGui(new RenderPreviewGui(OffsetSettingsGui.this, "offset"));
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
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.offset.title"), this.width / 2, 5, 16777215);
        super.draw(mouseX, mouseY, partialTicks);
    }
}