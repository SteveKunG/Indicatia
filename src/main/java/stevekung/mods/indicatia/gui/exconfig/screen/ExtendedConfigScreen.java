package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.DoubleConfigOption;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;

@OnlyIn(Dist.CLIENT)
public class ExtendedConfigScreen extends Screen
{
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();
    public static boolean PREVIEW;
    private Button resetButton;
    private Button doneButton;

    static
    {
        OPTIONS.add(ExtendedConfig.SWAP_INFO_POS);
        OPTIONS.add(ExtendedConfig.HEALTH_STATUS);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_ORDERING);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_DIRECTION);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_STATUS);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_POSITION);
        OPTIONS.add(ExtendedConfig.POTION_HUD_STYLE);
        OPTIONS.add(ExtendedConfig.POTION_HUD_POSITION);
        OPTIONS.add(ExtendedConfig.CPS_POSITION);
        OPTIONS.add(ExtendedConfig.CPS_OPACITY);
    }

    public ExtendedConfigScreen()
    {
        super(JsonUtils.create("Indicatia Extended Config"));
    }

    @Override
    public void init()
    {
        int i = 0;

        for (ExtendedConfigOption options : OPTIONS)
        {
            if (options instanceof DoubleConfigOption)
            {
                this.addButton(options.createOptionButton(this.width / 2 - 160 + i % 2 * 160, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            else
            {
                this.addButton(options.createOptionButton(this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            ++i;
        }

        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.render_info.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new RenderInfoSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("extended_config.custom_color.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new CustomColorSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.offset.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new OffsetSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("extended_config.hypixel.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new HypixelSettingsScreen(this));
        }));
        this.addButton(ExtendedConfig.PREVIEW.createOptionButton(this.width / 2 + 10, this.height / 6 + 103, 150));
        this.addButton(this.doneButton = new Button(this.width / 2 - 100, this.height / 6 + 175, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(null);
        }));
        this.addButton(this.resetButton = new Button(this.width / 2 + 10, this.height / 6 + 175, 100, 20, LangUtils.translate("extended_config.reset_config"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new ConfirmScreen(this::resetConfig, LangUtils.translateComponent("menu.reset_config_confirm"), JsonUtils.create("")));
        }));
        this.resetButton.visible = false;
    }

    @Override
    public void tick()
    {
        boolean shift = ClientUtils.isShiftKeyDown();

        if (shift)
        {
            this.doneButton.setWidth(100);
            this.doneButton.x = this.width / 2 - 105;
            this.resetButton.visible = true;
        }
        else
        {
            this.doneButton.setWidth(200);
            this.doneButton.x = this.width / 2 - 100;
            this.resetButton.visible = false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (!ExtendedConfigScreen.PREVIEW)
        {
            this.renderBackground();
        }
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.main.title") + " : " + LangUtils.translate("extended_config.current_profile.info", TextFormatting.YELLOW + ExtendedConfig.CURRENT_PROFILE + TextFormatting.RESET), this.width / 2, 10, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }

    private void resetConfig(boolean condition)
    {
        if (condition)
        {
            ExtendedConfig.resetConfig();
            this.minecraft.displayGuiScreen(this);
        }
        else
        {
            this.minecraft.displayGuiScreen(this);
        }
    }
}