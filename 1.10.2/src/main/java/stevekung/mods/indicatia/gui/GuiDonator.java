package stevekung.mods.indicatia.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.handler.HUDRenderHandler;
import stevekung.mods.indicatia.util.JsonUtil;
import stevekung.mods.indicatia.util.LangUtil;

public class GuiDonator extends GuiScreen
{
    private GuiTextField topDonateInput;
    private GuiTextField recentDonateInput;
    private GuiTextField topDonateTextInput;
    private GuiTextField recentDonateTextInput;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;
    private static TextFormatting[] values = TextFormatting.values();

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.topDonateInput = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 60, 300, 20);
        this.topDonateInput.setMaxStringLength(32767);
        this.topDonateInput.setCanLoseFocus(true);
        this.topDonateInput.setText(ExtendedConfig.TOP_DONATOR_FILE_PATH);

        this.recentDonateInput = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 85, 300, 20);
        this.recentDonateInput.setMaxStringLength(32767);
        this.recentDonateInput.setCanLoseFocus(true);
        this.recentDonateInput.setText(ExtendedConfig.RECENT_DONATOR_FILE_PATH);

        this.topDonateTextInput = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 110, 300, 20);
        this.topDonateTextInput.setMaxStringLength(32767);
        this.topDonateTextInput.setCanLoseFocus(true);
        this.topDonateTextInput.setText(ExtendedConfig.TOP_DONATOR_TEXT.replace("\u00a7", "&"));

        this.recentDonateTextInput = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 135, 300, 20);
        this.recentDonateTextInput.setMaxStringLength(32767);
        this.recentDonateTextInput.setCanLoseFocus(true);
        this.recentDonateTextInput.setText(ExtendedConfig.RECENT_DONATOR_TEXT.replace("\u00a7", "&"));

        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 120, 100, 20, LangUtil.translate("gui.done")));
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 120, 100, 20, LangUtil.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 120, 100, 20, "Reset Path"));
        this.resetBtn.enabled = !ExtendedConfig.TOP_DONATOR_FILE_PATH.isEmpty() || !ExtendedConfig.RECENT_DONATOR_FILE_PATH.isEmpty();
    }

    @Override
    public void updateScreen()
    {
        this.resetBtn.enabled = !ExtendedConfig.TOP_DONATOR_FILE_PATH.isEmpty() || !ExtendedConfig.RECENT_DONATOR_FILE_PATH.isEmpty();
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
        JsonUtil json = new JsonUtil();

        if (button.enabled)
        {
            if (button.id == 0)
            {
                if (!ExtendedConfig.TOP_DONATOR_FILE_PATH.equals(this.topDonateInput.getText()))
                {
                    this.mc.player.sendMessage(json.text("Set top donator file path to " + this.topDonateInput.getText()));
                }
                if (!ExtendedConfig.RECENT_DONATOR_FILE_PATH.equals(this.recentDonateInput.getText()))
                {
                    this.mc.player.sendMessage(json.text("Set recent donator file path to " + this.recentDonateInput.getText()));
                }
                ExtendedConfig.TOP_DONATOR_FILE_PATH = this.topDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.RECENT_DONATOR_FILE_PATH = this.recentDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.TOP_DONATOR_TEXT = this.convertString(this.topDonateTextInput.getText());
                ExtendedConfig.RECENT_DONATOR_TEXT = this.convertString(this.recentDonateTextInput.getText());
                ExtendedConfig.save();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                this.mc.player.sendMessage(json.text("Reset donator file path"));
                ExtendedConfig.TOP_DONATOR_FILE_PATH = "";
                ExtendedConfig.RECENT_DONATOR_FILE_PATH = "";
                HUDRenderHandler.topDonator = "";
                HUDRenderHandler.recentDonator = "";
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
        this.drawCenteredString(this.fontRendererObj, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Put your twitch donators file path. (.txt file only)", this.width / 2, 37, 10526880);
        this.drawString(this.fontRendererObj, "Top Donate:", this.width / 2 - 212, 66, 10526880);
        this.drawString(this.fontRendererObj, "Recent Donate:", this.width / 2 - 228, 90, 10526880);
        this.drawString(this.fontRendererObj, "Top Donate Text:", this.width / 2 - 238, 115, 10526880);
        this.drawString(this.fontRendererObj, "Recent Donate Text:", this.width / 2 - 254, 140, 10526880);
        this.drawCenteredString(this.fontRendererObj, TextFormatting.RESET + "Top Donate Text: " + this.convertString(this.topDonateTextInput.getText()), this.width / 2, 170, 10526880);
        this.drawCenteredString(this.fontRendererObj, TextFormatting.RESET + "Recent Donate Text: " + this.convertString(this.recentDonateTextInput.getText()), this.width / 2, 185, 10526880);
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
        for (TextFormatting formatting : GuiDonator.valuesCached())
        {
            if (original.contains("&" + formatting.formattingCode))
            {
                original = original.replace("&" + formatting.formattingCode, "\u00a7" + formatting.formattingCode);
            }
        }
        return original;
    }

    private static TextFormatting[] valuesCached()
    {
        return GuiDonator.values;
    }
}