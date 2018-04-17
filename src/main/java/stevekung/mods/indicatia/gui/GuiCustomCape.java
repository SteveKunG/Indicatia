package stevekung.mods.indicatia.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.ThreadDownloadedCustomCape;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiCustomCape extends GuiScreen
{
    private GuiTextField inputField;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;
    private GuiButton capeBtn;
    private int capeOption;
    private int prevCapeOption;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 + 85, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("message.reset_cape")));
        this.resetBtn.enabled = CapeUtils.pngFile.exists();

        if (!this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE) && !ExtendedConfig.showCustomCape)
        {
            this.capeOption = 0;
        }
        if (ExtendedConfig.showCustomCape)
        {
            this.capeOption = 1;
        }
        if (this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE))
        {
            this.capeOption = 2;
        }
        this.prevCapeOption = this.capeOption;
        this.capeBtn = this.addButton(new GuiButton(3, this.width / 2 + 50 + 4, this.height / 4 + 50, 100, 20, ""));
        this.setTextForCapeOption();
    }

    @Override
    public void updateScreen()
    {
        this.doneBtn.enabled = !this.inputField.getText().isEmpty() || this.prevCapeOption != this.capeOption;
        this.resetBtn.enabled = CapeUtils.pngFile.exists();
        this.setTextForCapeOption();
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
        if (button.enabled)
        {
            if (button.id == 0)
            {
                if (!this.inputField.getText().isEmpty())
                {
                    ThreadDownloadedCustomCape thread = new ThreadDownloadedCustomCape(this.inputField.getText());
                    thread.start();
                    this.mc.player.sendMessage(JsonUtils.create("Start downloading cape texture from " + this.inputField.getText()));
                }
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 1)
            {
                this.capeOption = this.prevCapeOption;
                this.saveCapeOption();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                CapeUtils.CAPE_TEXTURE.remove(GameProfileUtils.getUsername());
                this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("message.reset_current_cape")));
                CapeUtils.pngFile.delete();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 3)
            {
                int i = 0;
                i++;
                this.capeOption = (this.capeOption + i) % 3;
                this.saveCapeOption();
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
        GuiCustomCape.renderPlayer(this.mc, this.width, this.height);
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Custom Cape Downloader", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your Cape URL (Must be .png or image format)", this.width / 2, 37, 10526880);
        this.inputField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void setTextForCapeOption()
    {
        switch (this.capeOption)
        {
        case 0:
            this.capeBtn.displayString = "Cape: OFF";
            break;
        case 1:
            this.capeBtn.displayString = "Cape: Custom";
            break;
        case 2:
            this.capeBtn.displayString = "Cape: OptiFine";
            break;
        }
    }

    private void saveCapeOption()
    {
        if (this.capeOption == 0)
        {
            ExtendedConfig.showCustomCape = false;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 1)
        {
            ExtendedConfig.showCustomCape = true;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 2)
        {
            ExtendedConfig.showCustomCape = false;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
    }

    private static void renderPlayer(Minecraft mc, int width, int height)
    {
        float yawOffset = mc.player.renderYawOffset;
        float yaw = mc.player.rotationYaw;
        float pitch = mc.player.rotationPitch;
        float yawHead = mc.player.rotationYawHead;
        float scale = 40.0F + height / 8 - 28;
        RenderHelper.enableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2 - 50, height / 6 + 80, 256.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        mc.player.renderYawOffset = 0.0F;
        mc.player.rotationYaw = 0.0F;
        mc.player.rotationYawHead = mc.player.rotationYaw;
        GlStateManager.translate(0.0F, mc.player.getYOffset(), 0.0F);
        RenderManager manager = mc.getRenderManager();
        manager.setPlayerViewY(180.0F);
        manager.setRenderShadow(false);
        manager.renderEntity(mc.player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        manager.setRenderShadow(true);
        mc.player.renderYawOffset = yawOffset;
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
        mc.player.rotationYawHead = yawHead;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}