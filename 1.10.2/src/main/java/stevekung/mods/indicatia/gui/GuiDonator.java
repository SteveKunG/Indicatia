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
    private GuiTextField inputField;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, this.height / 4 - 20, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.inputField.setText(ExtendedConfig.TOP_DONATOR_FILE_PATH);
        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Path"));
        this.resetBtn.enabled = !ExtendedConfig.TOP_DONATOR_FILE_PATH.isEmpty();
    }

    @Override
    public void updateScreen()
    {
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.resetBtn.enabled = !this.inputField.getText().isEmpty();
        this.inputField.updateCursorCounter();
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
                if (!this.inputField.getText().isEmpty())
                {
                    ExtendedConfig.TOP_DONATOR_FILE_PATH = this.inputField.getText().replace("" + '\u0022', "");
                }
                this.mc.thePlayer.addChatMessage(json.text("Set donator file path to " + ExtendedConfig.TOP_DONATOR_FILE_PATH));
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                this.mc.thePlayer.addChatMessage(json.text("Reset donator file path"));
                ExtendedConfig.TOP_DONATOR_FILE_PATH = "";
                this.inputField.setText("");
                ExtendedConfig.save();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.inputField.textboxKeyTyped(typedChar, keyCode);

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
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Put your twitch donators file path, (.txt file only)", this.width / 2, 37, 10526880);
        this.inputField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}