package stevekung.mods.indicatia.renderer;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.math.MathHelper;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.KeystrokePosition;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.indicatia.utils.RenderUtilsIN;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

public class KeystrokeRenderer
{
    public static void render(MinecraftClient mc)
    {
        KeystrokeRenderer.renderDefaultStyle(mc, mc.window.getScaledWidth());
    }

    private static void renderDefaultStyle(MinecraftClient mc, int width)
    {
        width = ExtendedConfig.instance.keystrokePosition == KeystrokePosition.LEFT ? 96 : width;
        int widthSquare = 80;
        int heightSquare = 48 + ExtendedConfig.instance.keystrokeYOffset;
        boolean nullScreen = mc.currentScreen == null;
        boolean wDown = nullScreen && ClientUtils.isKeyDown(GLFW.GLFW_KEY_W);
        boolean aDown = nullScreen && ClientUtils.isKeyDown(GLFW.GLFW_KEY_A);
        boolean sDown = nullScreen && ClientUtils.isKeyDown(GLFW.GLFW_KEY_S);
        boolean dDown = nullScreen && ClientUtils.isKeyDown(GLFW.GLFW_KEY_D);
        boolean lmbDown = nullScreen && ClientUtils.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1);
        boolean rmbDown = nullScreen && ClientUtils.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_2);
        boolean sprintDown = mc.player.isSprinting();
        boolean sneakDown = mc.player.isSneaking();
        boolean blockDown = mc.player.method_6039();//TODO isActiveItemStackBlocking
        boolean useRainbow;
        int rainbow = Math.abs(MathHelper.hsvToRgb(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        float red = (rainbow >> 16 & 255) / 255.0F;
        float green = (rainbow >> 8 & 255) / 255.0F;
        float blue = (rainbow & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableDepthTest();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtilsIN.bindKeystrokeTexture("key_square");
        DrawableHelper.blit(width - widthSquare + 20, heightSquare, wDown ? 24 : 0, 0, 24, 24, 48, 24);
        DrawableHelper.blit(width - widthSquare - 4, heightSquare + 24, aDown ? 24 : 0, 0, 24, 24, 48, 24);
        DrawableHelper.blit(width - widthSquare + 20, heightSquare + 24, sDown ? 24 : 0, 0, 24, 24, 48, 24);
        DrawableHelper.blit(width - widthSquare + 44, heightSquare + 24, dDown ? 24 : 0, 0, 24, 24, 48, 24);
        useRainbow = ExtendedConfig.instance.keystrokeWASDRainbow;
        mc.textRenderer.draw("W", width - widthSquare + 29.0625F, heightSquare + 9, wDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit());
        mc.textRenderer.draw("A", width - widthSquare + 5.0625F, heightSquare + 32, aDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit());
        mc.textRenderer.draw("S", width - widthSquare + 29.0625F, heightSquare + 32, sDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit());
        mc.textRenderer.draw("D", width - widthSquare + 53.0625F, heightSquare + 32, dDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit());

        if (ExtendedConfig.instance.keystrokeMouse)
        {
            RenderUtilsIN.bindKeystrokeTexture("mouse_square");
            DrawableHelper.blit(width - widthSquare - 4, heightSquare - 12, lmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            DrawableHelper.blit(width - widthSquare + 44, heightSquare - 12, rmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            useRainbow = ExtendedConfig.instance.keystrokeMouseButtonRainbow;
            mc.textRenderer.draw("LMB", width - widthSquare - 0.5625F, heightSquare - 4, lmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeMouseButtonColor).to32Bit());
            mc.textRenderer.draw("RMB", width - widthSquare + 47.5625F, heightSquare - 4, rmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeMouseButtonColor).to32Bit());

            useRainbow = ExtendedConfig.instance.keystrokeCPSRainbow;

            if (ExtendedConfig.instance.cps && ExtendedConfig.instance.cpsPosition == CPSPosition.KEYSTROKE)
            {
                String cps = "CPS:" + InfoUtils.INSTANCE.getCPS();
                int smallFontWidth = ColorUtils.coloredFontRendererUnicode.getStringWidth(cps);

                if (lmbDown)
                {
                    ColorUtils.coloredFontRendererUnicode.draw(cps, width - widthSquare + 8.0625F - smallFontWidth / 2, heightSquare + 12, lmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeCPSColor).to32Bit());
                }
                else
                {
                    ColorUtils.coloredFontRendererUnicode.drawWithShadow(cps, width - widthSquare + 8.0625F - smallFontWidth / 2, heightSquare + 12, lmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeCPSColor).to32Bit());
                }
            }

            useRainbow = ExtendedConfig.instance.keystrokeRCPSRainbow;

            if (ExtendedConfig.instance.rcps && ExtendedConfig.instance.cpsPosition == CPSPosition.KEYSTROKE)
            {
                String rcps = "RCPS:" + InfoUtils.INSTANCE.getRCPS();
                int smallFontWidth = ColorUtils.coloredFontRendererUnicode.getStringWidth(rcps);

                if (rmbDown)
                {
                    ColorUtils.coloredFontRendererUnicode.draw(rcps, width - widthSquare + 56.0625F - smallFontWidth / 2, heightSquare + 12, rmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeRCPSColor).to32Bit());
                }
                else
                {
                    ColorUtils.coloredFontRendererUnicode.drawWithShadow(rcps, width - widthSquare + 56.0625F - smallFontWidth / 2, heightSquare + 12, rmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeRCPSColor).to32Bit());
                }
            }
        }
        if (ExtendedConfig.instance.keystrokeSprintSneak)
        {
            RenderUtilsIN.bindKeystrokeTexture("button_square_2");
            DrawableHelper.blit(width - widthSquare + 2, heightSquare + 48, sprintDown ? 20 : 0, 0, 20, 20, 40, 20);
            DrawableHelper.blit(width - widthSquare + 22, heightSquare + 48, sneakDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("sprint");
            useRainbow = ExtendedConfig.instance.keystrokeSprintRainbow;
            GlStateManager.color3f(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatBlue());
            DrawableHelper.blit(width - widthSquare + 2, heightSquare + 48, sprintDown ? 0 : 20, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("sneak");
            useRainbow = ExtendedConfig.instance.keystrokeSneakRainbow;
            GlStateManager.color3f(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatBlue());
            DrawableHelper.blit(width - widthSquare + 22, heightSquare + 48, sneakDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        if (ExtendedConfig.instance.keystrokeBlocking)
        {
            RenderUtilsIN.bindKeystrokeTexture("button_square_2");
            DrawableHelper.blit(width - widthSquare + 42, heightSquare + 48, blockDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("block");
            useRainbow = ExtendedConfig.instance.keystrokeBlockingRainbow;
            GlStateManager.color3f(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatBlue());
            DrawableHelper.blit(width - widthSquare + 42, heightSquare + 48, blockDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableDepthTest();
    }
}