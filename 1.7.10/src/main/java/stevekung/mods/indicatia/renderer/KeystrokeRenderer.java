package stevekung.mods.indicatia.renderer;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.RenderUtil;

public class KeystrokeRenderer
{
    public static void init(Minecraft mc)
    {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        KeystrokeRenderer.renderStyleNormal(mc, res.getScaledWidth());
    }

    private static void renderStyleNormal(Minecraft mc, int width)
    {
        width = ConfigManager.keystrokePosition.equalsIgnoreCase("left") ? 96 : width;
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
        boolean blockDown = mc.thePlayer.isBlocking();
        boolean useRainbow = false;
        int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        float red = (rainbow >> 16 & 255) / 255.0F;
        float green = (rainbow >> 8 & 255) / 255.0F;
        float blue = (rainbow & 255) / 255.0F;
        float r = 0;
        float g = 0;
        float b = 0;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        RenderUtil.bindKeystrokeTexture("key_square");
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare, wDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare + 24, aDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare + 24, sDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare + 24, dDown ? 24 : 0, 0, 24, 24, 48, 24);
        r = ExtendedConfig.KEYSTROKE_WASD_RED;
        g = ExtendedConfig.KEYSTROKE_WASD_GREEN;
        b = ExtendedConfig.KEYSTROKE_WASD_BLUE;
        useRainbow = ExtendedConfig.KEYSTROKE_WASD_RAINBOW;
        mc.fontRendererObj.drawString("W", (int) (width - widthSquare + 29.5F), heightSquare + 9, wDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("A", (int) (width - widthSquare + 5.5F), heightSquare + 32, aDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("S", (int) (width - widthSquare + 29.5F), heightSquare + 32, sDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
        mc.fontRendererObj.drawString("D", (int) (width - widthSquare + 53.5F), heightSquare + 32, dDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);

        if (ConfigManager.enableKeystrokeLMBRMB)
        {
            RenderUtil.bindKeystrokeTexture("mouse_square");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare - 12, lmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare - 12, rmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            r = ExtendedConfig.KEYSTROKE_LMBRMB_RED;
            g = ExtendedConfig.KEYSTROKE_LMBRMB_GREEN;
            b = ExtendedConfig.KEYSTROKE_LMBRMB_BLUE;
            useRainbow = ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW;
            mc.fontRendererObj.drawString("LMB", (int) (width - widthSquare - 0.5F), heightSquare - 4, lmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);
            mc.fontRendererObj.drawString("RMB", (int) (width - widthSquare + 47.5F), heightSquare - 4, rmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), false);

            r = ExtendedConfig.KEYSTROKE_CPS_RED;
            g = ExtendedConfig.KEYSTROKE_CPS_GREEN;
            b = ExtendedConfig.KEYSTROKE_CPS_BLUE;
            useRainbow = ExtendedConfig.KEYSTROKE_CPS_RAINBOW;

            IndicatiaMod.coloredFontRenderer.setUnicodeFlag(true);

            if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String cps = "CPS:" + InfoUtil.INSTANCE.getCPS();
                int smallFontWidth = IndicatiaMod.coloredFontRenderer.getStringWidth(cps);
                IndicatiaMod.coloredFontRenderer.drawString(cps, (int) (width - widthSquare + 8.5F - smallFontWidth / 2), heightSquare + 12, lmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), lmbDown ? false : true);
            }
            if (ConfigManager.enableRCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String rcps = "RCPS:" + InfoUtil.INSTANCE.getRCPS();
                int smallFontWidth = IndicatiaMod.coloredFontRenderer.getStringWidth(rcps);
                IndicatiaMod.coloredFontRenderer.drawString(rcps, (int) (width - widthSquare + 56.5F - smallFontWidth / 2), heightSquare + 12, rmbDown ? 0 : useRainbow ? rainbow : RenderUtil.to32BitColor(255, (int)r, (int)g, (int)b), rmbDown ? false : true);
            }
            IndicatiaMod.coloredFontRenderer.setUnicodeFlag(false);
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
            GL11.glColor3f(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 0 : 20, 0, 20, 20, 40, 20);

            RenderUtil.bindKeystrokeTexture("sneak");
            r = ExtendedConfig.KEYSTROKE_SNEAK_RED / 255.0F;
            g = ExtendedConfig.KEYSTROKE_SNEAK_GREEN / 255.0F;
            b = ExtendedConfig.KEYSTROKE_SNEAK_BLUE / 255.0F;
            useRainbow = ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW;
            GL11.glColor3f(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
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
            GL11.glColor3f(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
}