package stevekung.mods.indicatia.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.Collections;

public class GuiAutoLoginFunction extends GuiScreen
{
    private GuiTextField inputField;
    private GuiButtonCustomizeTexture helpBtn;
    private ServerData data;

    public GuiAutoLoginFunction()
    {
        this.data = Minecraft.getInstance().getCurrentServerData();
    }

    @Override
    public void initGui()
    {
        this.mc.keyboardListener.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 + 65, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.addButton(new GuiButton(0, this.width / 2 - 152, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                if (GuiAutoLoginFunction.this.data != null)
                {
                    GuiAutoLoginFunction.this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_function_set")));
                    ExtendedConfig.loginData.removeAutoLogin(GameProfileUtils.getUUID() + GuiAutoLoginFunction.this.data.serverIP);
                    ExtendedConfig.loginData.addAutoLogin(GuiAutoLoginFunction.this.data.serverIP, "", "", GameProfileUtils.getUUID(), GuiAutoLoginFunction.this.inputField.getText());
                    ExtendedConfig.save();
                }
                GuiAutoLoginFunction.this.mc.displayGuiScreen(null);
            }
        });
        this.addButton(new GuiButton(1, this.width / 2 + 2, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.cancel"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiAutoLoginFunction.this.mc.displayGuiScreen(null);
            }
        });
        this.addButton(this.helpBtn = new GuiButtonCustomizeTexture(2, this.width / 2 + 130, this.height / 4 + 35, this, Collections.singletonList(LangUtils.translate("message.help")), "help")
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiAutoLoginFunction.this.mc.displayGuiScreen(new GuiAutoLoginFunctionHelp(true));
            }
        });

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
        this.children.add(this.inputField);
    }

    @Override
    public void tick()
    {
        this.inputField.tick();
    }

    @Override
    public void onGuiClosed()
    {
        this.mc.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "Auto Login Function", this.width / 2, this.height / 4, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your own bot function to make it run automatically", this.width / 2, this.height / 4 + 20, 10526880);
        this.inputField.drawTextField(mouseX, mouseY, partialTicks);
        this.helpBtn.drawRegion(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}