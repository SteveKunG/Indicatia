package stevekung.mods.indicatia.renderer;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.RenderUtil;

public class KeystrokeRenderer
{
    private static final SmallFontRenderer smallFontRenderer = new SmallFontRenderer();

    public static void init(Minecraft mc)
    {
        ScaledResolution res = new ScaledResolution(mc);
        KeystrokeRenderer.renderStyleNormal(mc, KeystrokeRenderer.smallFontRenderer, res.getScaledWidth());
    }

    private static void renderStyleNormal(Minecraft mc, SmallFontRenderer smallFontRenderer, int width)
    {
        width = ConfigManager.keystrokePosition.equals("left") ? 96 : width;
        int widthSquare = 80;
        int heightSquare = 48 + ExtendedConfig.KEYSTROKE_Y_OFFSET;
        boolean nullScreen = mc.currentScreen == null;
        boolean wDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean aDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean sDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean dDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean lmbDown = nullScreen && Mouse.isButtonDown(0);
        boolean rmbDown = nullScreen && Mouse.isButtonDown(1);
        boolean sprintDown = mc.thePlayer.isSprinting();
        boolean sneakDown = mc.thePlayer.isSneaking();
        boolean blockDown = mc.thePlayer.isActiveItemStackBlocking();
        boolean useRainbow = false;
        int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        float red = (rainbow >> 16 & 255) / 255.0F;
        float green = (rainbow >> 8 & 255) / 255.0F;
        float blue = (rainbow & 255) / 255.0F;
        float r = 0;
        float g = 0;
        float b = 0;

        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtil.bindKeystrokeTexture("key_square");
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare, wDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare + 24, aDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare + 24, sDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare + 24, dDown ? 24 : 0, 0, 24, 24, 48, 24);
        r = ExtendedConfig.KEYSTROKE_WASD_RED;
        g = ExtendedConfig.KEYSTROKE_WASD_GREEN;
        b = ExtendedConfig.KEYSTROKE_WASD_BLUE;
        useRainbow = ExtendedConfig.KEYSTROKE_WASD_RAINBOW;
        mc.fontRendererObj.drawString("W", width - widthSquare + 29.0625F, heightSquare + 9, wDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("A", width - widthSquare + 5.0625F, heightSquare + 32, aDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("S", width - widthSquare + 29.0625F, heightSquare + 32, sDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("D", width - widthSquare + 53.0625F, heightSquare + 32, dDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);

        if (ConfigManager.enableKeystrokeLMBRMB)
        {
            RenderUtil.bindKeystrokeTexture("mouse_square");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare - 12, lmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare - 12, rmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            r = ExtendedConfig.KEYSTROKE_LMBRMB_RED;
            g = ExtendedConfig.KEYSTROKE_LMBRMB_GREEN;
            b = ExtendedConfig.KEYSTROKE_LMBRMB_BLUE;
            useRainbow = ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW;
            mc.fontRendererObj.drawString("LMB", width - widthSquare - 0.5625F, heightSquare - 4, lmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
            mc.fontRendererObj.drawString("RMB", width - widthSquare + 47.5625F, heightSquare - 4, rmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);

            r = ExtendedConfig.KEYSTROKE_CPS_RED;
            g = ExtendedConfig.KEYSTROKE_CPS_GREEN;
            b = ExtendedConfig.KEYSTROKE_CPS_BLUE;
            useRainbow = ExtendedConfig.KEYSTROKE_CPS_RAINBOW;

            if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String cps = "CPS:" + InfoUtil.INSTANCE.getCPS();
                int smallFontWidth = smallFontRenderer.getStringWidth(cps);
                smallFontRenderer.drawString(cps, width - widthSquare + 8.0625F - smallFontWidth / 2, heightSquare + 12, lmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), lmbDown ? false : true);
            }
            if (ConfigManager.enableRCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String rcps = "RCPS:" + InfoUtil.INSTANCE.getRCPS();
                int smallFontWidth = smallFontRenderer.getStringWidth(rcps);
                smallFontRenderer.drawString(rcps, width - widthSquare + 56.0625F - smallFontWidth / 2, heightSquare + 12, rmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), rmbDown ? false : true);
            }
        }
        if (ConfigManager.enableKeystrokeSprintSneak)
        {
            RenderUtil.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 20 : 0, 0, 20, 20, 40, 20);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtil.bindKeystrokeTexture("sprint");
            r = ExtendedConfig.KEYSTROKE_SPRINT_RED / 255.0F;
            g = ExtendedConfig.KEYSTROKE_SPRINT_GREEN / 255.0F;
            b = ExtendedConfig.KEYSTROKE_SPRINT_BLUE / 255.0F;
            useRainbow = ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 0 : 20, 0, 20, 20, 40, 20);

            RenderUtil.bindKeystrokeTexture("sneak");
            r = ExtendedConfig.KEYSTROKE_SNEAK_RED / 255.0F;
            g = ExtendedConfig.KEYSTROKE_SNEAK_GREEN / 255.0F;
            b = ExtendedConfig.KEYSTROKE_SNEAK_BLUE / 255.0F;
            useRainbow = ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        if (ConfigManager.enableKeystrokeBlocking)
        {
            RenderUtil.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 20 : 0, 0, 20, 20, 40, 20);

            RenderUtil.bindKeystrokeTexture("block");
            r = ExtendedConfig.KEYSTROKE_BLOCK_RED / 255.0F;
            g = ExtendedConfig.KEYSTROKE_BLOCK_GREEN / 255.0F;
            b = ExtendedConfig.KEYSTROKE_BLOCK_BLUE / 255.0F;
            useRainbow = ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }
}