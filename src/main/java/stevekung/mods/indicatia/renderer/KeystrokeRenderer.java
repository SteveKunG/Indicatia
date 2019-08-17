package stevekung.mods.indicatia.renderer;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.KeystrokePosition;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.indicatia.utils.RenderUtilsIN;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class KeystrokeRenderer
{
    public static void render(Minecraft mc)
    {
        ScaledResolution res = new ScaledResolution(mc);
        KeystrokeRenderer.renderDefaultStyle(mc, res.getScaledWidth());
    }

    private static void renderDefaultStyle(Minecraft mc, int width)
    {
        width = KeystrokePosition.getById(ExtendedConfig.instance.keystrokePosition).equalsIgnoreCase("left") ? 96 : width;
        int widthSquare = 80;
        int heightSquare = 48 + ExtendedConfig.instance.keystrokeYOffset;
        boolean nullScreen = mc.currentScreen == null;
        boolean wDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean aDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean sDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean dDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean lmbDown = nullScreen && Mouse.isButtonDown(0);
        boolean rmbDown = nullScreen && Mouse.isButtonDown(1);
        boolean sprintDown = mc.player.isSprinting();
        boolean sneakDown = mc.player.isSneaking();
        boolean blockDown = mc.player.isActiveItemStackBlocking();
        boolean useRainbow = false;
        int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        float red = (rainbow >> 16 & 255) / 255.0F;
        float green = (rainbow >> 8 & 255) / 255.0F;
        float blue = (rainbow & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtilsIN.bindKeystrokeTexture("key_square");
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare, wDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare + 24, aDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare + 24, sDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare + 24, dDown ? 24 : 0, 0, 24, 24, 48, 24);
        useRainbow = ExtendedConfig.instance.keystrokeWASDRainbow;
        mc.fontRenderer.drawString("W", width - widthSquare + 29.0625F, heightSquare + 9, wDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit(), false);
        mc.fontRenderer.drawString("A", width - widthSquare + 5.0625F, heightSquare + 32, aDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit(), false);
        mc.fontRenderer.drawString("S", width - widthSquare + 29.0625F, heightSquare + 32, sDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit(), false);
        mc.fontRenderer.drawString("D", width - widthSquare + 53.0625F, heightSquare + 32, dDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeWASDColor).to32Bit(), false);

        if (ExtendedConfig.instance.keystrokeMouse)
        {
            RenderUtilsIN.bindKeystrokeTexture("mouse_square");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare - 12, lmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare - 12, rmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            useRainbow = ExtendedConfig.instance.keystrokeMouseButtonRainbow;
            mc.fontRenderer.drawString("LMB", width - widthSquare - 0.5625F, heightSquare - 4, lmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeMouseButtonColor).to32Bit(), false);
            mc.fontRenderer.drawString("RMB", width - widthSquare + 47.5625F, heightSquare - 4, rmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeMouseButtonColor).to32Bit(), false);

            useRainbow = ExtendedConfig.instance.keystrokeCPSRainbow;

            if (ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("keystroke"))
            {
                String cps = "CPS:" + InfoUtils.INSTANCE.getCPS();
                int smallFontWidth = ColorUtils.unicodeFontRenderer.getStringWidth(cps);
                ColorUtils.unicodeFontRenderer.drawString(cps, width - widthSquare + 8.0625F - smallFontWidth / 2, heightSquare + 12, lmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeCPSColor).to32Bit(), lmbDown ? false : true);
            }

            useRainbow = ExtendedConfig.instance.keystrokeRCPSRainbow;

            if (ExtendedConfig.instance.rcps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("keystroke"))
            {
                String rcps = "RCPS:" + InfoUtils.INSTANCE.getRCPS();
                int smallFontWidth = ColorUtils.unicodeFontRenderer.getStringWidth(rcps);
                ColorUtils.unicodeFontRenderer.drawString(rcps, width - widthSquare + 56.0625F - smallFontWidth / 2, heightSquare + 12, rmbDown ? 0 : useRainbow ? rainbow : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeRCPSColor).to32Bit(), rmbDown ? false : true);
            }
        }
        if (ExtendedConfig.instance.keystrokeSprintSneak)
        {
            RenderUtilsIN.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 20 : 0, 0, 20, 20, 40, 20);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("sprint");
            useRainbow = ExtendedConfig.instance.keystrokeSprintRainbow;
            GlStateManager.color(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSprintColor).floatBlue());
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 0 : 20, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("sneak");
            useRainbow = ExtendedConfig.instance.keystrokeSneakRainbow;
            GlStateManager.color(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeSneakColor).floatBlue());
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        if (ExtendedConfig.instance.keystrokeBlocking)
        {
            RenderUtilsIN.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtilsIN.bindKeystrokeTexture("block");
            useRainbow = ExtendedConfig.instance.keystrokeBlockingRainbow;
            GlStateManager.color(useRainbow ? red : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatRed(), useRainbow ? green : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatGreen(), useRainbow ? blue : ColorUtils.stringToRGB(ExtendedConfig.instance.keystrokeBlockingColor).floatBlue());
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }
}