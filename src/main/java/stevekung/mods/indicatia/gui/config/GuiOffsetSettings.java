package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class GuiOffsetSettings extends Screen
{
    private final Screen parent;
    private GuiConfigButtonRowList optionsRowList;
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

    GuiOffsetSettings(Screen parent)
    {
        super(JsonUtils.create("Offset Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.save();
            GuiOffsetSettings.this.minecraft.openScreen(GuiOffsetSettings.this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            ExtendedConfig.save();
            GuiOffsetSettings.this.minecraft.openScreen(new GuiRenderPreview(GuiOffsetSettings.this, "offset"));
        }));

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new GuiConfigButtonRowList(this.width, this.height, 32, this.height - 32, 25, options);
        this.children.add(this.optionsRowList);
    }

    @Override
    public Element getFocused()
    {
        return this.optionsRowList;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.offset.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}