package stevekung.mods.indicatia.gui;

import java.util.Collections;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.server.integrated.IntegratedServer;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class GuiAutoLoginFunction extends Screen
{
    private TextFieldWidget inputField;
    private GuiButtonCustomizeTexture helpBtn;
    private IntegratedServer data;

    public GuiAutoLoginFunction()
    {
        super(JsonUtils.create("Auto Login Function"));
        this.data = MinecraftClient.getInstance().getServer();
    }

    @Override
    public void init()
    {
        this.minecraft.keyboard.enableRepeatEvents(true);
        this.inputField = new TextFieldWidget(this.font, this.width / 2 - 150, this.height / 4 + 65, 300, 20, "Auto Login Input");
        this.inputField.setMaxLength(32767);
        this.addButton(new ButtonWidget(this.width / 2 - 152, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.done"), button ->
        {
            if (GuiAutoLoginFunction.this.data != null)
            {
                GuiAutoLoginFunction.this.minecraft.player.addChatMessage(JsonUtils.create(LangUtils.translate("commands.auto_login.function_set")), false);
                ExtendedConfig.loginData.removeAutoLogin(GameProfileUtils.getUUID() + GuiAutoLoginFunction.this.data.getServerIp());
                ExtendedConfig.loginData.addAutoLogin(GuiAutoLoginFunction.this.data.getServerIp(), "", "", GameProfileUtils.getUUID(), GuiAutoLoginFunction.this.inputField.getText());
                ExtendedConfig.instance.save();
            }
            GuiAutoLoginFunction.this.minecraft.openScreen(null);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 2, this.height / 4 + 100, 150, 20, LangUtils.translate("gui.cancel"), button ->
        {
            GuiAutoLoginFunction.this.minecraft.openScreen(null);
        }));
        this.addButton(this.helpBtn = new GuiButtonCustomizeTexture(this.width / 2 + 130, this.height / 4 + 35, this, Collections.singletonList(LangUtils.translate("menu.help")), "help", button ->
        {
            GuiAutoLoginFunction.this.minecraft.openScreen(new GuiAutoLoginFunctionHelp(true));
        }));

        if (this.data != null)
        {
            ExtendedConfig.loginData.getAutoLoginList().forEach(login ->
            {
                if (this.data.getServerIp().equalsIgnoreCase(login.getServerIP()) && GameProfileUtils.getUUID().equals(login.getUUID()) && !login.getFunction().isEmpty())
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
    public void onClose()
    {
        this.minecraft.keyboard.enableRepeatEvents(false);
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
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.minecraft.textRenderer, "Auto Login Function", this.width / 2, this.height / 4, 16777215);
        this.drawCenteredString(this.minecraft.textRenderer, "Put your own bot function to make it run automatically", this.width / 2, this.height / 4 + 20, 10526880);
        this.inputField.render(mouseX, mouseY, partialTicks);
        this.helpBtn.drawRegion(mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}