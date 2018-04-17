package stevekung.mods.indicatia.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.stevekunglib.utils.CachedEnum;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiDonator extends GuiScreen
{
    private GuiTextField topDonateInput;
    private GuiTextField recentDonateInput;
    private GuiTextField topDonateTextInput;
    private GuiTextField recentDonateTextInput;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.topDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 60, 220, 20);
        this.topDonateInput.setMaxStringLength(32767);
        this.topDonateInput.setCanLoseFocus(true);
        this.topDonateInput.setText(ExtendedConfig.topDonatorFilePath);

        this.recentDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 85, 220, 20);
        this.recentDonateInput.setMaxStringLength(32767);
        this.recentDonateInput.setCanLoseFocus(true);
        this.recentDonateInput.setText(ExtendedConfig.recentDonatorFilePath);

        this.topDonateTextInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 110, 220, 20);
        this.topDonateTextInput.setMaxStringLength(32767);
        this.topDonateTextInput.setCanLoseFocus(true);
        this.topDonateTextInput.setText(ExtendedConfig.topDonatorText.replace("\u00a7", "&"));

        this.recentDonateTextInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 135, 220, 20);
        this.recentDonateTextInput.setMaxStringLength(32767);
        this.recentDonateTextInput.setCanLoseFocus(true);
        this.recentDonateTextInput.setText(ExtendedConfig.recentDonatorText.replace("\u00a7", "&"));

        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height - 38, 100, 20, LangUtils.translate("gui.done")));
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height - 38, 100, 20, LangUtils.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height - 38, 100, 20, LangUtils.translate("message.reset_path")));
        this.resetBtn.enabled = !ExtendedConfig.topDonatorFilePath.isEmpty() || !ExtendedConfig.recentDonatorFilePath.isEmpty();
    }

    @Override
    public void updateScreen()
    {
        this.resetBtn.enabled = !ExtendedConfig.topDonatorFilePath.isEmpty() || !ExtendedConfig.recentDonatorFilePath.isEmpty();
        this.topDonateInput.updateCursorCounter();
        this.recentDonateInput.updateCursorCounter();
        this.topDonateTextInput.updateCursorCounter();
        this.recentDonateTextInput.updateCursorCounter();
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                if (!ExtendedConfig.topDonatorFilePath.equals(this.topDonateInput.getText()))
                {
                    this.mc.player.sendMessage(JsonUtils.create("Set top donator file path to " + this.topDonateInput.getText()));
                }
                if (!ExtendedConfig.recentDonatorFilePath.equals(this.recentDonateInput.getText()))
                {
                    this.mc.player.sendMessage(JsonUtils.create("Set recent donator file path to " + this.recentDonateInput.getText()));
                }
                ExtendedConfig.topDonatorFilePath = this.topDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.recentDonatorFilePath = this.recentDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.topDonatorText = this.convertString(this.topDonateTextInput.getText());
                ExtendedConfig.recentDonatorText = this.convertString(this.recentDonateTextInput.getText());
                ExtendedConfig.save();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("message.reset_donator_file_path")));
                ExtendedConfig.topDonatorFilePath = "";
                ExtendedConfig.recentDonatorFilePath = "";
                HUDRenderEventHandler.topDonator = "";
                HUDRenderEventHandler.recentDonator = "";
                this.topDonateInput.setText("");
                this.recentDonateInput.setText("");
                ExtendedConfig.save();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.topDonateInput.textboxKeyTyped(typedChar, keyCode);
        this.recentDonateInput.textboxKeyTyped(typedChar, keyCode);
        this.topDonateTextInput.textboxKeyTyped(typedChar, keyCode);
        this.recentDonateTextInput.textboxKeyTyped(typedChar, keyCode);

        if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        }
        else
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.topDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.topDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your twitch donators file path. (.txt file only)", this.width / 2, 37, 10526880);
        this.drawString(this.fontRenderer, "Top Donate:", this.width / 2 - 145, 66, 10526880);
        this.drawString(this.fontRenderer, "Recent Donate:", this.width / 2 - 160, 90, 10526880);
        this.drawString(this.fontRenderer, "Top Donate Text:", this.width / 2 - 170, 115, 10526880);
        this.drawString(this.fontRenderer, "Recent Donate Text:", this.width / 2 - 185, 140, 10526880);
        this.drawCenteredString(this.fontRenderer, TextFormatting.RESET + "Top Donate Text: " + this.convertString(this.topDonateTextInput.getText()), this.width / 2, 170, 10526880);
        this.drawCenteredString(this.fontRenderer, TextFormatting.RESET + "Recent Donate Text: " + this.convertString(this.recentDonateTextInput.getText()), this.width / 2, 185, 10526880);
        this.topDonateInput.drawTextBox();
        this.recentDonateInput.drawTextBox();
        this.topDonateTextInput.drawTextBox();
        this.recentDonateTextInput.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
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