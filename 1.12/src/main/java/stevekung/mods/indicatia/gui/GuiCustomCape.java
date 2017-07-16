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
import stevekung.mods.indicatia.utils.*;

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
        this.inputField.setText(ExtendedConfig.CAPE_URL.isEmpty() ? "" : Base64Utils.decode(ExtendedConfig.CAPE_URL));
        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.cancel")));
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Cape"));
        this.resetBtn.enabled = !ExtendedConfig.CAPE_URL.isEmpty();

        if (!this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE) && !ExtendedConfig.SHOW_CAPE)
        {
            this.capeOption = 0;
        }
        if (ExtendedConfig.SHOW_CAPE)
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
        this.resetBtn.enabled = !ExtendedConfig.CAPE_URL.isEmpty();
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
                    CapeUtils.textureUploaded = true;
                    CapeUtils.setCapeURL(this.inputField.getText(), false);
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
                ExtendedConfig.CAPE_URL = "";
                CapeUtils.CAPE_TEXTURE.remove(GameProfileUtil.getUsername());
                this.mc.player.sendMessage(new JsonUtil().text("Reset cape texture"));
                this.inputField.setText("");
                ExtendedConfig.save();
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
            ExtendedConfig.SHOW_CAPE = false;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 1)
        {
            ExtendedConfig.SHOW_CAPE = true;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 2)
        {
            ExtendedConfig.SHOW_CAPE = false;
            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
    }

    private static void renderPlayer(Minecraft mc, int width, int height)
    {
        float f2 = mc.player.renderYawOffset;
        float f3 = mc.player.rotationYaw;
        float f4 = mc.player.rotationPitch;
        float f5 = mc.player.rotationYawHead;
        float scale = 40.0F + height / 8 - 28;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2 - 50, height / 4 + 55, 0.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        mc.player.renderYawOffset = 0.0F;
        mc.player.rotationYaw = (float) Math.atan(19 / 40.0F) * 40.0F;
        mc.player.rotationYaw = 0.0F;
        mc.player.rotationYawHead = mc.player.rotationYaw;
        GlStateManager.translate(0.0F, (float) mc.player.getYOffset(), 0.0F);
        RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(mc.player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        mc.player.renderYawOffset = f2;
        mc.player.rotationYaw = f3;
        mc.player.rotationPitch = f4;
        mc.player.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}