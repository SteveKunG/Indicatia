package stevekung.mods.indicatia.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.JsonUtil;
import stevekung.mods.indicatia.utils.LangUtil;

public class GuiDonator extends GuiScreen
{
    private GuiTextField topDonateInput;
    private GuiTextField recentDonateInput;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.topDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 - 20, 300, 20);
        this.topDonateInput.setMaxStringLength(32767);
        this.topDonateInput.setFocused(false);
        this.topDonateInput.setCanLoseFocus(true);
        this.topDonateInput.setText(ExtendedConfig.TOP_DONATOR_FILE_PATH);

        this.recentDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 + 20, 300, 20);
        this.recentDonateInput.setMaxStringLength(32767);
        this.recentDonateInput.setFocused(false);
        this.recentDonateInput.setCanLoseFocus(true);
        this.recentDonateInput.setText(ExtendedConfig.RECENT_DONATOR_FILE_PATH);

        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.done")));
        this.doneBtn.enabled = !this.topDonateInput.getText().isEmpty() || !this.recentDonateInput.getText().isEmpty();
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Path"));
        this.resetBtn.enabled = !ExtendedConfig.TOP_DONATOR_FILE_PATH.isEmpty();
    }

    @Override
    public void updateScreen()
    {
        this.doneBtn.enabled = !this.topDonateInput.getText().isEmpty() || !this.recentDonateInput.getText().isEmpty();
        this.resetBtn.enabled = !this.topDonateInput.getText().isEmpty() || !this.recentDonateInput.getText().isEmpty();
        this.topDonateInput.updateCursorCounter();
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
                if (!this.topDonateInput.getText().isEmpty())
                {
                    ExtendedConfig.TOP_DONATOR_FILE_PATH = this.topDonateInput.getText().replace("" + '\u0022', "");
                    this.mc.player.sendMessage(json.text("Set top donator file path to " + ExtendedConfig.TOP_DONATOR_FILE_PATH));
                }
                if (!this.recentDonateInput.getText().isEmpty())
                {
                    ExtendedConfig.RECENT_DONATOR_FILE_PATH = this.recentDonateInput.getText().replace("" + '\u0022', "");
                    this.mc.player.sendMessage(json.text("Set recent donator file path to " + ExtendedConfig.RECENT_DONATOR_FILE_PATH));
                }
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
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your twitch donators file path. (.txt file only)", this.width / 2, 37, 10526880);
        this.drawString(this.fontRenderer, "Top Donate:", this.width / 2 - 212, this.height / 4 - 15, 10526880);
        this.drawString(this.fontRenderer, "Recent Donate:", this.width / 2 - 228, this.height / 4 + 25, 10526880);
        this.topDonateInput.drawTextBox();
        this.recentDonateInput.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}