package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiEventListener;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TextFormat;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RenderInfoSettingsGui extends Gui
{
    private final Gui parent;
//    private TestListWidget optionsRowList;
//    private static final ExtendedConfig.Options[] OPTIONS;
//
//    static
//    {
//        OPTIONS = new ExtendedConfig.Options[] {ExtendedConfig.Options.FPS, ExtendedConfig.Options.XYZ, ExtendedConfig.Options.DIRECTION, ExtendedConfig.Options.BIOME, ExtendedConfig.Options.PING, ExtendedConfig.Options.PING_TO_SECOND, ExtendedConfig.Options.SERVER_IP,
//                ExtendedConfig.Options.SERVER_IP_MC, ExtendedConfig.Options.EQUIPMENT_HUD, ExtendedConfig.Options.POTION_HUD, ExtendedConfig.Options.KEYSTROKE, ExtendedConfig.Options.KEYSTROKE_LRMB, ExtendedConfig.Options.KEYSTROKE_SS, ExtendedConfig.Options.KEYSTROKE_BLOCKING,
//                ExtendedConfig.Options.CPS, ExtendedConfig.Options.RCPS ,ExtendedConfig.Options.SLIME_CHUNK, ExtendedConfig.Options.REAL_TIME, ExtendedConfig.Options.GAME_TIME, ExtendedConfig.Options.GAME_WEATHER, ExtendedConfig.Options.MOON_PHASE, ExtendedConfig.Options.POTION_ICON,
//                ExtendedConfig.Options.TPS, ExtendedConfig.Options.TPS_ALL_DIMS, ExtendedConfig.Options.ALTERNATE_POTION_COLOR};
//    }

    public RenderInfoSettingsGui(Gui parent)
    {
        this.parent = parent;
        this.client = MinecraftClient.getInstance();
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
                RenderInfoSettingsGui.this.client.openGui(RenderInfoSettingsGui.this.parent);
            }
        });
//        this.optionsRowList = new TestListWidget(this.width, this.height, 32, this.height - 32, 25, OPTIONS);
//        this.listeners.add(this.optionsRowList);
    }

//    @Nullable
//    @Override
//    public GuiEventListener getFocused()
//    {
//        return this.optionsRowList;
//    }

    @Override
    public boolean charTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        return super.charTyped(typedChar, keyCode);
    }

//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int state)
//    {
//        int scale = this.client.options.guiScale;
//
//        if (super.mouseReleased(mouseX, mouseY, state))
//        {
//            return true;
//        }
//        else if (this.optionsRowList != null && this.optionsRowList.mouseReleased(mouseX, mouseY, state))
//        {
//            if (this.client.options.guiScale != scale)
//            {
//                this.client.onResolutionChanged();
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int state)
//    {
//        return false;
//        int scale = this.client.options.guiScale;
//
//        if (super.mouseClicked(mouseX, mouseY, state))
//        {
//            if (this.client.options.guiScale != scale)
//            {
//                this.client.onResolutionChanged();
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.render_info.title"), this.width / 2, 5, 16777215);

//        if (this.optionsRowList != null)
//        {
//            this.optionsRowList.draw(mouseX, mouseY, partialTicks);
//        }
//        if (ConfigListWidget.comment != null)
//        {
//            List<String> wrappedLine = this.fontRenderer.wrapStringToWidthAsList(ConfigListWidget.comment, 250);
//            int y = 15;
//
//            for (String text : wrappedLine)
//            {
//                this.drawStringCentered(this.fontRenderer, TextFormat.GREEN + text, this.width / 2, y, 16777215);
//                y += this.fontRenderer.fontHeight;
//            }
//        }
//        else
//        {
//            this.drawStringCentered(this.fontRenderer, TextFormat.YELLOW + LangUtils.translate("extended_config.render_info.rclick.info"), this.width / 2, 15, 16777215);
//        }
        super.draw(mouseX, mouseY, partialTicks);
    }
}