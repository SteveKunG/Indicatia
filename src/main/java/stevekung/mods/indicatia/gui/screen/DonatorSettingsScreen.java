package stevekung.mods.indicatia.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.enums.CachedEnum;

@OnlyIn(Dist.CLIENT)
public class DonatorSettingsScreen extends Screen
{
    private TextFieldWidget topDonateInput;
    private TextFieldWidget recentDonateInput;
    private TextFieldWidget topDonateTextInput;
    private TextFieldWidget recentDonateTextInput;
    private Button resetBtn;

    public DonatorSettingsScreen()
    {
        super(JsonUtils.create("Donator"));
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.topDonateInput = new TextFieldWidget(this.font, this.width / 2 - 80, 60, 220, 20, "");
        this.topDonateInput.setMaxStringLength(32767);
        this.topDonateInput.setCanLoseFocus(true);
        this.topDonateInput.setText(ExtendedConfig.instance.topDonatorFilePath);

        this.recentDonateInput = new TextFieldWidget(this.font, this.width / 2 - 80, 85, 220, 20, "");
        this.recentDonateInput.setMaxStringLength(32767);
        this.recentDonateInput.setCanLoseFocus(true);
        this.recentDonateInput.setText(ExtendedConfig.instance.recentDonatorFilePath);

        this.topDonateTextInput = new TextFieldWidget(this.font, this.width / 2 - 80, 110, 220, 20, "");
        this.topDonateTextInput.setMaxStringLength(32767);
        this.topDonateTextInput.setCanLoseFocus(true);
        this.topDonateTextInput.setText(ExtendedConfig.instance.topDonatorText.replace("\u00a7", "&"));

        this.recentDonateTextInput = new TextFieldWidget(this.font, this.width / 2 - 80, 135, 220, 20, "");
        this.recentDonateTextInput.setMaxStringLength(32767);
        this.recentDonateTextInput.setCanLoseFocus(true);
        this.recentDonateTextInput.setText(ExtendedConfig.instance.recentDonatorText.replace("\u00a7", "&"));

        this.addButton(new Button(this.width / 2 - 50 - 100 - 4, this.height - 38, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            if (!ExtendedConfig.instance.topDonatorFilePath.equals(this.topDonateInput.getText()))
            {
                this.minecraft.player.sendMessage(JsonUtils.create("Set top donator file path to " + this.topDonateInput.getText()));
            }
            if (!ExtendedConfig.instance.recentDonatorFilePath.equals(this.recentDonateInput.getText()))
            {
                this.minecraft.player.sendMessage(JsonUtils.create("Set recent donator file path to " + this.recentDonateInput.getText()));
            }
            ExtendedConfig.instance.topDonatorFilePath = this.topDonateInput.getText().replace("" + '\u0022', "");
            ExtendedConfig.instance.recentDonatorFilePath = this.recentDonateInput.getText().replace("" + '\u0022', "");
            ExtendedConfig.instance.topDonatorText = this.convertString(this.topDonateTextInput.getText());
            ExtendedConfig.instance.recentDonatorText = this.convertString(this.recentDonateTextInput.getText());
            ExtendedConfig.instance.save();
            this.minecraft.displayGuiScreen(null);
        }));
        this.addButton(new Button(this.width / 2 + 50 + 4, this.height - 38, 100, 20, LangUtils.translate("gui.cancel"), button -> this.minecraft.displayGuiScreen(null)));
        this.resetBtn = this.addButton(new Button(this.width / 2 - 50, this.height - 38, 100, 20, LangUtils.translate("menu.reset_path"), button ->
        {
            this.minecraft.player.sendMessage(JsonUtils.create(LangUtils.translate("menu.reset_donator_path")));
            ExtendedConfig.instance.topDonatorFilePath = "";
            ExtendedConfig.instance.recentDonatorFilePath = "";
            HUDRenderEventHandler.topDonator = "";
            HUDRenderEventHandler.recentDonator = "";
            this.topDonateInput.setText("");
            this.recentDonateInput.setText("");
            ExtendedConfig.instance.save();
        }));
        this.resetBtn.active = !ExtendedConfig.instance.topDonatorFilePath.isEmpty() || !ExtendedConfig.instance.recentDonatorFilePath.isEmpty();
        this.children.add(this.topDonateInput);
        this.children.add(this.recentDonateInput);
        this.children.add(this.topDonateTextInput);
        this.children.add(this.recentDonateTextInput);
    }

    @Override
    public void tick()
    {
        this.resetBtn.active = !ExtendedConfig.instance.topDonatorFilePath.isEmpty() || !ExtendedConfig.instance.recentDonatorFilePath.isEmpty();
        this.topDonateInput.tick();
        this.recentDonateInput.tick();
        this.topDonateTextInput.tick();
        this.recentDonateTextInput.tick();
    }

    @Override
    public void onClose()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        this.topDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.topDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.font, "Put your twitch donators file path. (.txt file only)", this.width / 2, 37, 10526880);
        this.drawString(this.font, "Top Donate:", this.width / 2 - 145, 66, 10526880);
        this.drawString(this.font, "Recent Donate:", this.width / 2 - 160, 90, 10526880);
        this.drawString(this.font, "Top Donate Text:", this.width / 2 - 170, 115, 10526880);
        this.drawString(this.font, "Recent Donate Text:", this.width / 2 - 185, 140, 10526880);
        this.drawCenteredString(this.font, TextFormatting.RESET + "Top Donate Text: " + this.convertString(this.topDonateTextInput.getText()), this.width / 2, 170, 10526880);
        this.drawCenteredString(this.font, TextFormatting.RESET + "Recent Donate Text: " + this.convertString(this.recentDonateTextInput.getText()), this.width / 2, 185, 10526880);
        this.topDonateInput.render(mouseX, mouseY, partialTicks);
        this.recentDonateInput.render(mouseX, mouseY, partialTicks);
        this.topDonateTextInput.render(mouseX, mouseY, partialTicks);
        this.recentDonateTextInput.render(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    private String convertString(String original)
    {
        for (TextFormatting formatting : CachedEnum.textFormatValues)
        {
            if (original.contains("&" + formatting.formattingCode))
            {
                original = original.replace("&" + formatting.formattingCode, "\u00a7" + formatting.formattingCode);
            }
        }
        return original;
    }
}