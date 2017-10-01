package stevekung.mods.indicatia.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.ModLogger;

@SideOnly(Side.CLIENT)
public class ColoredFontRenderer extends FontRenderer
{
    private static int marker = 59136;
    private boolean dropShadow;
    private int state = 0;
    private int red;
    private int green;
    private int blue;
    private final String specialUpperChars = "\u0e48\u0e49\u0e4a\u0e4b";
    private final String upperChars = "\u0e31\u0e34\u0e35\u0e36\u0e37\u0e47\u0e4c\u0e4d\u0e4e" + this.specialUpperChars;
    private final String lowerChars = "\u0e38\u0e39\u0e3a";

    public ColoredFontRenderer(GameSettings gameSettings, ResourceLocation location, TextureManager textureManager, boolean unicode)
    {
        super(gameSettings, location, textureManager, unicode);
        ModLogger.info("Loading {} for fancy text!", this.getClass().getName());
    }

    @Override
    protected String wrapFormattedStringToWidth(String str, int wrapWidth)
    {
        int i = this.sizeStringToWidth(str, wrapWidth);

        if (str.length() <= i)
        {
            return str;
        }
        else
        {
            String s = str.substring(0, i);
            char c0 = str.charAt(i);
            boolean flag = c0 == ' ' || c0 == '\n';
            String s1 = this.getCustomFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
            return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    @Override
    public int renderString(String text, float x, float y, int color, boolean dropShadow)
    {
        this.dropShadow = dropShadow;
        return super.renderString(text, x, y, color, dropShadow);
    }

    @Override
    protected float renderUnicodeChar(char charac, boolean italic)
    {
        return this.renderColoredChar(charac, super.renderUnicodeChar(charac, italic));
    }

    @Override
    protected float renderDefaultChar(int charac, boolean italic)
    {
        return this.renderColoredChar(charac, super.renderDefaultChar(charac, italic));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        super.onResourceManagerReload(resourceManager);
        this.setUnicodeFlag(IndicatiaMod.MC.getLanguageManager().isCurrentLocaleUnicode() || IndicatiaMod.MC.gameSettings.forceUnicodeFont);
        this.setBidiFlag(IndicatiaMod.MC.getLanguageManager().isCurrentLanguageBidirectional());
    }

    @Override
    public float renderChar(char charac, boolean italic)
    {
        float value = Float.NaN;

        if (this.upperChars.indexOf(charac) != -1 || this.lowerChars.indexOf(charac) != -1)
        {
            value = this.renderThaiCharacter(charac, italic);
        }
        if (Float.isNaN(value))
        {
            value = super.renderChar(charac, italic);
        }
        return value;
    }

    @Override
    public int getCharWidth(char charac)
    {
        if (this.upperChars.indexOf(charac) != -1 || this.lowerChars.indexOf(charac) != -1)
        {
            return 0;
        }
        return super.getCharWidth(charac);
    }

    public static String color(int r, int g, int b)
    {
        return String.format("%c%c%c", (char) (ColoredFontRenderer.marker + (r & 255)), (char) (ColoredFontRenderer.marker + (g & 255)), (char) (ColoredFontRenderer.marker + (b & 255)));
    }

    private float renderThaiCharacter(char charac, boolean italic)
    {
        this.loadGlyphTexture(14);
        float posYShift = 0.0F;
        float height = 2.99F;

        if (this.lowerChars.indexOf(charac) != -1)
        {
            height = 1.99F;
            posYShift = 6.0F;
        }

        float heightX2 = height * 2;
        int rawWidth = this.glyphWidth[charac] & 0xFF;
        float startTexcoordX = rawWidth >>> 4;
        float charWidth = (rawWidth & 15) + 1;
        float texcoordX = charac % 16 * 16 + startTexcoordX;
        float texcoordY = (charac & 255) / 16 * 16 + posYShift * 2;
        float texcoordXEnd = charWidth - startTexcoordX - 0.02F;
        float skew = italic ? 1.0F : 0.0F;
        float posX = this.posX - ((charWidth - startTexcoordX) / 2.0F + 0.5F);
        float posY = this.posY + posYShift;

        GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);
        GlStateManager.glTexCoord2f(texcoordX / 256.0F, texcoordY / 256.0F);
        GlStateManager.glVertex3f(posX + skew, posY, 0.0F);
        GlStateManager.glTexCoord2f(texcoordX / 256.0F, (texcoordY + heightX2) / 256.0F);
        GlStateManager.glVertex3f(posX - skew, posY + height, 0.0F);
        GlStateManager.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, texcoordY / 256.0F);
        GlStateManager.glVertex3f(posX + texcoordXEnd / 2.0F + skew, posY, 0.0F);
        GlStateManager.glTexCoord2f((texcoordX + texcoordXEnd) / 256.0F, (texcoordY + heightX2) / 256.0F);
        GlStateManager.glVertex3f(posX + texcoordXEnd / 2.0F - skew, posY + height, 0.0F);
        GlStateManager.glEnd();
        return 0.0F;
    }

    private float renderColoredChar(int charac, float defaultValue)
    {
        if (charac >= ColoredFontRenderer.marker && charac <= ColoredFontRenderer.marker + 255)
        {
            int value = charac & 255;

            switch (this.state)
            {
            case 0:
                this.red = value;
                break;
            case 1:
                this.green = value;
                break;
            case 2:
                this.blue = value;
                break;
            default:
                this.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                return 0.0F;
            }

            this.state = ++this.state % 3;
            int color = this.red << 16 | this.green << 8 | this.blue | 255 << 24;

            if ((color & -67108864) == 0)
            {
                color |= -16777216;
            }
            if (this.dropShadow)
            {
                color = (color & 16579836) >> 2 | color & -16777216;
            }
            this.setColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color >> 0 & 255) / 255.0F, (color >> 24 & 255) / 255.0F);
            return 0.0F;
        }
        if (this.state != 0)
        {
            this.state = 0;
            this.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        return defaultValue;
    }

    private String getCustomFormatFromString(String text)
    {
        String s = "";
        int i = 0;
        int j = text.length();

        while (i < j - 1)
        {
            char c = text.charAt(i);

            if (c == 167)
            {
                char c0 = text.charAt(i + 1);

                if (c0 >= 48 && c0 <= 57 || c0 >= 97 && c0 <= 102 || c0 >= 65 && c0 <= 70)
                {
                    s = "\u00a7" + c0;
                    i++;
                }
                else if (c0 >= 107 && c0 <= 111 || c0 >= 75 && c0 <= 79 || c0 == 114 || c0 == 82)
                {
                    s = s + "\u00a7" + c0;
                    i++;
                }
            }
            else if (c >= ColoredFontRenderer.marker && c <= ColoredFontRenderer.marker + 255)
            {
                s = String.format("%s%s%s", c, text.charAt(i + 1), text.charAt(i + 2));
                i += 2;
            }
            i++;
        }
        return s;
    }
}