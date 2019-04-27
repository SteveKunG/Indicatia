//package stevekung.mods.indicatia.gui;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.GuiTextField;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.entity.player.EnumPlayerModelParts;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import stevekung.mods.indicatia.config.ExtendedConfig;
//import stevekung.mods.indicatia.utils.CapeUtils;
//import stevekung.mods.indicatia.utils.ThreadDownloadedCustomCape;
//import stevekung.mods.stevekungslib.utils.JsonUtils;
//import stevekung.mods.stevekungslib.utils.LangUtils;
//
//@OnlyIn(Dist.CLIENT)
//public class GuiCustomCape extends GuiScreen
//{
//    private GuiTextField inputField;
//    private GuiButton doneBtn;
//    private GuiButton resetBtn;
//    private GuiButton capeBtn;
//    private int capeOption;
//    private int prevCapeOption;
//
//    @Override
//    public void initGui()
//    {
//        this.mc.keyboardListener.enableRepeatEvents(true);
//        this.inputField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, this.height / 4 + 85, 300, 20);
//        this.inputField.setMaxStringLength(32767);
//        this.inputField.setFocused(true);
//        this.inputField.setCanLoseFocus(true);
//        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("gui.done"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                if (!GuiCustomCape.this.inputField.getText().isEmpty())
//                {
//                    ThreadDownloadedCustomCape thread = new ThreadDownloadedCustomCape(GuiCustomCape.this.inputField.getText());
//                    thread.start();
//                    GuiCustomCape.this.mc.player.sendMessage(JsonUtils.create("Start downloading cape texture from " + GuiCustomCape.this.inputField.getText()));
//                }
//                GuiCustomCape.this.mc.displayGuiScreen(null);
//            }
//        });
//        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
//        this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("gui.cancel"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                GuiCustomCape.this.capeOption = GuiCustomCape.this.prevCapeOption;
//                GuiCustomCape.this.saveCapeOption();
//                GuiCustomCape.this.mc.displayGuiScreen(null);
//            }
//        });
//        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, LangUtils.translate("menu.reset_cape"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                CapeUtils.CAPE_TEXTURE = null;
//                GuiCustomCape.this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("menu.reset_current_cape")));
//                CapeUtils.texture.delete();
//                GuiCustomCape.this.mc.displayGuiScreen(null);
//            }
//        });
//        this.resetBtn.enabled = CapeUtils.texture.exists();
//
//        if (!this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE) && !ExtendedConfig.showCustomCape)
//        {
//            this.capeOption = 0;
//        }
//        if (ExtendedConfig.showCustomCape)
//        {
//            this.capeOption = 1;
//        }
//        if (this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE))
//        {
//            this.capeOption = 2;
//        }
//        this.prevCapeOption = this.capeOption;
//        this.capeBtn = this.addButton(new GuiButton(3, this.width / 2 + 50 + 4, this.height / 4 + 50, 100, 20, "")
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                int i = 0;
//                i++;
//                GuiCustomCape.this.capeOption = (GuiCustomCape.this.capeOption + i) % 3;
//                GuiCustomCape.this.saveCapeOption();
//            }
//        });
//        this.children.add(this.inputField);
//        this.setTextForCapeOption();
//    }
//
//    @Override
//    public void tick()
//    {
//        this.doneBtn.enabled = !this.inputField.getText().isEmpty() || this.prevCapeOption != this.capeOption;
//        this.resetBtn.enabled = CapeUtils.texture.exists();
//        this.setTextForCapeOption();
//        this.inputField.tick();
//    }
//
//    @Override
//    public void onGuiClosed()
//    {
//        this.mc.keyboardListener.enableRepeatEvents(false);
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
//    {
//        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
//        return super.mouseClicked(mouseX, mouseY, mouseButton);
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks)
//    {
//        GuiCustomCape.renderPlayer(this.mc, this.width, this.height);
//        this.drawDefaultBackground();
//        this.drawCenteredString(this.fontRenderer, "Custom Cape Downloader", this.width / 2, 20, 16777215);
//        this.drawCenteredString(this.fontRenderer, "Put your Cape URL (Must be .png or image format)", this.width / 2, 37, 10526880);
//        this.inputField.drawTextField(mouseX, mouseY, partialTicks);
//        super.render(mouseX, mouseY, partialTicks);
//    }
//
//    @Override
//    public boolean doesGuiPauseGame()
//    {
//        return false;
//    }
//
//    private void setTextForCapeOption()
//    {
//        switch (this.capeOption)
//        {
//        case 0:
//            this.capeBtn.displayString = "Cape: OFF";
//            break;
//        case 1:
//            this.capeBtn.displayString = "Cape: Custom";
//            break;
//        case 2:
//            this.capeBtn.displayString = "Cape: OptiFine";
//            break;
//        }
//    }
//
//    private void saveCapeOption()
//    {
//        if (this.capeOption == 0)
//        {
//            ExtendedConfig.showCustomCape = false;
//            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
//        }
//        if (this.capeOption == 1)
//        {
//            ExtendedConfig.showCustomCape = true;
//            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
//        }
//        if (this.capeOption == 2)
//        {
//            ExtendedConfig.showCustomCape = false;
//            this.mc.gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
//        }
//        this.mc.gameSettings.sendSettingsToServer();
//        this.mc.gameSettings.saveOptions();
//        ExtendedConfig.save();
//    }
//
//    private static void renderPlayer(Minecraft mc, int width, int height)
//    {
//        float yawOffset = mc.player.renderYawOffset;
//        float yaw = mc.player.rotationYaw;
//        float pitch = mc.player.rotationPitch;
//        float yawHead = mc.player.rotationYawHead;
//        float scale = 40.0F + height / 8 - 28;
//        RenderHelper.enableStandardItemLighting();
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef(width / 2 - 50, height / 6 + 85, 256.0F);
//        GlStateManager.scalef(-scale, scale, scale);
//        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
//        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
//        mc.player.renderYawOffset = 0.0F;
//        mc.player.rotationYaw = 0.0F;
//        mc.player.rotationYawHead = mc.player.rotationYaw;
//        GlStateManager.translated(0.0D, mc.player.getYOffset(), 0.0D);
//        RenderManager manager = mc.getRenderManager();
//        manager.setPlayerViewY(180.0F);
//        manager.setRenderShadow(false);
//        manager.renderEntity(mc.player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
//        manager.setRenderShadow(true);
//        mc.player.renderYawOffset = yawOffset;
//        mc.player.rotationYaw = yaw;
//        mc.player.rotationPitch = pitch;
//        mc.player.rotationYawHead = yawHead;
//        GlStateManager.popMatrix();
//        RenderHelper.disableStandardItemLighting();
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.activeTexture(OpenGlHelper.GL_TEXTURE1);
//        GlStateManager.disableTexture2D();
//        GlStateManager.activeTexture(OpenGlHelper.GL_TEXTURE0);
//    }
//}