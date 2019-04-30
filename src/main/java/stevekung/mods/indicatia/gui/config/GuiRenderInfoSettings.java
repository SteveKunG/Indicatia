package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TextFormat;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class GuiRenderInfoSettings extends Screen
{
    private final Screen parent;
    private GuiConfigButtonRowList optionsRowList;
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.Options.FPS);
        OPTIONS.add(ExtendedConfig.Options.XYZ);
        OPTIONS.add(ExtendedConfig.Options.DIRECTION);
        OPTIONS.add(ExtendedConfig.Options.BIOME);
        OPTIONS.add(ExtendedConfig.Options.PING);
        OPTIONS.add(ExtendedConfig.Options.PING_TO_SECOND);
        OPTIONS.add(ExtendedConfig.Options.SERVER_IP);
        OPTIONS.add(ExtendedConfig.Options.SERVER_IP_MC);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_HUD);
        OPTIONS.add(ExtendedConfig.Options.POTION_HUD);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_LRMB);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SS);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING);
        OPTIONS.add(ExtendedConfig.Options.CPS);
        OPTIONS.add(ExtendedConfig.Options.RCPS);
        OPTIONS.add(ExtendedConfig.Options.SLIME_CHUNK);
        OPTIONS.add(ExtendedConfig.Options.REAL_TIME);
        OPTIONS.add(ExtendedConfig.Options.GAME_TIME);
        OPTIONS.add(ExtendedConfig.Options.GAME_WEATHER);
        OPTIONS.add(ExtendedConfig.Options.MOON_PHASE);
        OPTIONS.add(ExtendedConfig.Options.POTION_ICON);
        OPTIONS.add(ExtendedConfig.Options.TPS);
        OPTIONS.add(ExtendedConfig.Options.TPS_ALL_DIMS);
        OPTIONS.add(ExtendedConfig.Options.ALTERNATE_POTION_COLOR);
    }

    GuiRenderInfoSettings(Screen parent)
    {
        super(JsonUtils.create("Render Info Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 150, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.save();
            GuiRenderInfoSettings.this.minecraft.openScreen(GuiRenderInfoSettings.this.parent);
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
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.render_info.title"), this.width / 2, 5, 16777215);

        if (GuiConfigButtonRowList.comment != null)
        {
            List<String> wrappedLine = this.minecraft.textRenderer.wrapStringToWidthAsList(GuiConfigButtonRowList.comment, 250);
            int y = 15;

            for (String text : wrappedLine)
            {
                this.drawCenteredString(this.minecraft.textRenderer, TextFormat.GREEN + text, this.width / 2, y, 16777215);
                y += this.minecraft.textRenderer.fontHeight;
            }
        }
        else
        {
            this.drawCenteredString(this.minecraft.textRenderer, TextFormat.YELLOW + LangUtils.translate("extended_config.render_info.rclick.info"), this.width / 2, 15, 16777215);
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}