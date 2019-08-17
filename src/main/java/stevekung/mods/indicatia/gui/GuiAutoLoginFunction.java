package stevekung.mods.indicatia.gui;

import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiAutoLoginFunction extends GuiScreen
{
    private GuiTextField inputField;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButtonCustomizeTexture helpBtn;
    private ServerData data;

    public GuiAutoLoginFunction()
    {
        this.data = Minecraft.getMinecraft().getCurrentServerData();
    }

    public void display()
    {
        this.data = Minecraft.getMinecraft().getCurrentServerData();
        CommonUtils.registerEventHandler(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        Minecraft.getMinecraft().displayGuiScreen(this);
        CommonUtils.unregisterEventHandler(this);
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 + 65, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 152, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.done")));
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 2, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.cancel")));
        this.buttonList.add(this.helpBtn = new GuiButtonCustomizeTexture(2, this.width / 2 + 130, this.height / 4 + 35, this, Arrays.asList(LangUtils.translate("message.help")), "help"));

        if (this.data != null)
        {
            ExtendedConfig.loginData.getAutoLoginList().forEach(login ->
            {
                if (this.data.serverIP.equalsIgnoreCase(login.getServerIP()) && GameProfileUtils.getUUID().equals(login.getUUID()) && !login.getFunction().isEmpty())
                {
                    this.inputField.setText(login.getFunction());
                }
            });
        }
    }

    @Override
    public void updateScreen()
    {
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
        if (button.id == 0)
        {
            if (this.data != null)
            {
                this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_function_set")));
                ExtendedConfig.loginData.removeAutoLogin(GameProfileUtils.getUUID() + this.data.serverIP);
                ExtendedConfig.loginData.addAutoLogin(this.data.serverIP, "", "", GameProfileUtils.getUUID(), this.inputField.getText());
                ExtendedConfig.instance.save();
            }
            this.mc.displayGuiScreen(null);
        }
        if (button.id == 1)
        {
            this.mc.displayGuiScreen(null);
        }
        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiAutoLoginFunctionHelp(true));
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
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "Auto Login Function", this.width / 2, this.height / 4, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your own bot function to make it run automatically", this.width / 2, this.height / 4 + 20, 10526880);
        this.inputField.drawTextBox();
        this.helpBtn.drawRegion(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}