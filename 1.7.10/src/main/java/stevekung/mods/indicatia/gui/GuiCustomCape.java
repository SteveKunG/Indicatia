package stevekung.mods.indicatia.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
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
        this.inputField = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, this.height / 4 + 85, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.inputField.setText(ExtendedConfig.CAPE_URL.isEmpty() ? "" : Base64Utils.decode(ExtendedConfig.CAPE_URL));
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtil.translate("gui.cancel")));
        this.buttonList.add(this.resetBtn = new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Cape"));
        this.resetBtn.enabled = !ExtendedConfig.CAPE_URL.isEmpty();

        if (!this.mc.gameSettings.showCape && !ExtendedConfig.SHOW_CAPE)
        {
            this.capeOption = 0;
        }
        if (ExtendedConfig.SHOW_CAPE)
        {
            this.capeOption = 1;
        }
        if (this.mc.gameSettings.showCape)
        {
            this.capeOption = 2;
        }
        this.prevCapeOption = this.capeOption;
        this.buttonList.add(this.capeBtn = new GuiButton(3, this.width / 2 + 50 + 4, this.height / 4 + 50, 100, 20, ""));
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
    protected void actionPerformed(GuiButton button)
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
                this.mc.thePlayer.addChatMessage(new JsonUtil().text("Reset cape texture"));
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
    protected void keyTyped(char typedChar, int keyCode)
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GuiCustomCape.renderPlayer(this.mc, this.width, this.height);
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Custom Cape Downloader", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Put your Cape URL (Must be .png or image format)", this.width / 2, 37, 10526880);
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
            this.mc.gameSettings.showCape = false;
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 1)
        {
            ExtendedConfig.SHOW_CAPE = true;
            this.mc.gameSettings.showCape = false;
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
        if (this.capeOption == 2)
        {
            ExtendedConfig.SHOW_CAPE = false;
            this.mc.gameSettings.showCape = true;
            this.mc.gameSettings.sendSettingsToServer();
            this.mc.gameSettings.saveOptions();
            ExtendedConfig.save();
        }
    }

    private static void renderPlayer(Minecraft mc, int width, int height)
    {
        float f2 = mc.thePlayer.renderYawOffset;
        float f3 = mc.thePlayer.rotationYaw;
        float f4 = mc.thePlayer.rotationPitch;
        float f5 = mc.thePlayer.rotationYawHead;
        float scale = 40.0F + height / 8 - 28;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2 - 50, height / 4 + 55, 256.0F);
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        mc.thePlayer.renderYawOffset = 0.0F;
        mc.thePlayer.rotationYaw = (float) Math.atan(19 / 40.0F) * 40.0F;
        mc.thePlayer.rotationYaw = 0.0F;
        mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, mc.thePlayer.yOffset - 0.35F, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        mc.thePlayer.renderYawOffset = f2;
        mc.thePlayer.rotationYaw = f3;
        mc.thePlayer.rotationPitch = f4;
        mc.thePlayer.rotationYawHead = f5;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}