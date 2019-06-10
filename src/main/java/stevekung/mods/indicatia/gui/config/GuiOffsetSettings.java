package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiOffsetSettings extends Screen
{
    private final Screen parent;
    private GuiConfigButtonRowList optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.ARMOR_HUD_Y);
        OPTIONS.add(ExtendedConfig.POTION_HUD_Y);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_Y);
        OPTIONS.add(ExtendedConfig.MAXIMUM_POTION_DISPLAY);
        OPTIONS.add(ExtendedConfig.POTION_LENGTH_Y_OFFSET);
        OPTIONS.add(ExtendedConfig.POTION_LENGTH_Y_OFFSET_OVERLAP);
    }

    GuiOffsetSettings(Screen parent)
    {
        super(JsonUtils.create("Offset Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.instance.save();
            GuiOffsetSettings.this.minecraft.displayGuiScreen(GuiOffsetSettings.this.parent);
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            ExtendedConfig.instance.save();
            GuiOffsetSettings.this.minecraft.displayGuiScreen(new GuiRenderPreview(GuiOffsetSettings.this, "offset"));
        }));

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new GuiConfigButtonRowList(this.width, this.height, 32, this.height - 32, 25, options);
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
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.offset.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}