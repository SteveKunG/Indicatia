//package stevekung.mods.indicatia.mixin;
//
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.BossInfoClient;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.GuiBossOverlay;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.BossInfo;
//import net.minecraftforge.client.ForgeHooksClient;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import stevekung.mods.indicatia.config.ConfigManagerIN;
//
//@Mixin(GuiBossOverlay.class)
//public abstract class GuiBossOverlayMixin extends Gui
//{
//    @Shadow
//    @Final
//    private Minecraft client;
//
//    @Shadow
//    @Final
//    private static ResourceLocation GUI_BARS_TEXTURES;
//
//    @Shadow
//    protected abstract void render(int x, int y, BossInfo info);
//
//    @Overwrite
//    public void renderBossHealth()
//    {
//        boolean render = ConfigManagerIN.indicatia_general.enableRenderBossHealthBar;
//
//        if (!this.client.ingameGUI.getBossOverlay().mapBossInfos.isEmpty())
//        {
//            ScaledResolution scaledresolution = new ScaledResolution(this.client);
//            int i = scaledresolution.getScaledWidth();
//            int j = 12;
//
//            for (BossInfoClient bossInfo : this.client.ingameGUI.getBossOverlay().mapBossInfos.values())
//            {
//                int k = i / 2 - 91;
//                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(scaledresolution, bossInfo, k, j, 10 + this.client.fontRenderer.FONT_HEIGHT);
//
//                if (!event.isCanceled())
//                {
//                    if (render)
//                    {
//                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                        this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
//                        this.render(k, j, bossInfo);
//                    }
//                    String s = bossInfo.getName().getFormattedText();
//                    this.client.fontRenderer.drawStringWithShadow(s, i / 2 - this.client.fontRenderer.getStringWidth(s) / 2, j - 9, 16777215);
//                }
//
//                j += !render ? 12 : event.getIncrement();
//                ForgeHooksClient.bossBarRenderPost(scaledresolution);
//
//                if (!render ? j >= scaledresolution.getScaledHeight() / 4.5D : j >= scaledresolution.getScaledHeight() / 3)
//                {
//                    break;
//                }
//            }
//        }
//    }
//}