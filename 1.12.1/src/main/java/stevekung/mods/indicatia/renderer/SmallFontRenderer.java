package stevekung.mods.indicatia.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class SmallFontRenderer implements IResourceManagerReloadListener
{
    private static final ResourceLocation[] UNICODE_PAGE_LOCATIONS = new ResourceLocation[256];
    protected final int[] charWidth = new int[256];
    public final int FONT_HEIGHT = 9;
    private final Random fontRandom = new Random();
    private final byte[] glyphWidth = new byte[65536];
    private final int[] colorCode = new int[32];
    private static final ResourceLocation LOCATION_FONT_TEXTURE = new ResourceLocation("textures/font/ascii.png");
    private float posX;
    private float posY;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;

    public SmallFontRenderer()
    {
        for (int i = 0; i < 32; ++i)
        {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6)
            {
                k += 85;
            }
            if (IndicatiaMod.MC.gameSettings.anaglyph)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16)
            {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture()
    {
        IResource iresource = null;
        BufferedImage bufferedimage;

        try
        {
            iresource = this.getResource(SmallFontRenderer.LOCATION_FONT_TEXTURE);
            bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        finally
        {
            IOUtils.closeQuietly(iresource);
        }

        int lvt_3_1_ = bufferedimage.getWidth();
        int lvt_4_1_ = bufferedimage.getHeight();
        int[] lvt_5_1_ = new int[lvt_3_1_ * lvt_4_1_];
        bufferedimage.getRGB(0, 0, lvt_3_1_, lvt_4_1_, lvt_5_1_, 0, lvt_3_1_);
        int lvt_6_1_ = lvt_4_1_ / 16;
        int lvt_7_1_ = lvt_3_1_ / 16;
        float lvt_9_1_ = 8.0F / lvt_7_1_;

        for (int lvt_10_1_ = 0; lvt_10_1_ < 256; ++lvt_10_1_)
        {
            int j1 = lvt_10_1_ % 16;
            int k1 = lvt_10_1_ / 16;

            if (lvt_10_1_ == 32)
            {
                this.charWidth[lvt_10_1_] = 4;
            }

            int l1;

            for (l1 = lvt_7_1_ - 1; l1 >= 0; --l1)
            {
                int i2 = j1 * lvt_7_1_ + l1;
                boolean flag1 = true;

                for (int j2 = 0; j2 < lvt_6_1_ && flag1; ++j2)
                {
                    int k2 = (k1 * lvt_7_1_ + j2) * lvt_3_1_;

                    if ((lvt_5_1_[i2 + k2] >> 24 & 255) != 0)
                    {
                        flag1 = false;
                    }
                }
                if (!flag1)
                {
                    break;
                }
            }
            ++l1;
            this.charWidth[lvt_10_1_] = (int)(0.5D + l1 * lvt_9_1_) + 1;
        }
    }

    private void readGlyphSizes()
    {
        IResource iresource = null;

        try
        {
            iresource = this.getResource(new ResourceLocation("font/glyph_sizes.bin"));
            iresource.getInputStream().read(this.glyphWidth);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            IOUtils.closeQuietly(iresource);
        }
    }

    private float renderChar(char ch, boolean italic)
    {
        if (ch == 32)
        {
            return 4.0F;
        }
        else
        {
            return this.renderUnicodeChar(ch, italic);
        }
    }

    private ResourceLocation getUnicodePageLocation(int page)
    {
        if (SmallFontRenderer.UNICODE_PAGE_LOCATIONS[page] == null)
        {
            SmallFontRenderer.UNICODE_PAGE_LOCATIONS[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] {Integer.valueOf(page)}));
        }
        return SmallFontRenderer.UNICODE_PAGE_LOCATIONS[page];
    }

    private void loadGlyphTexture(int page)
    {
        this.bindTexture(this.getUnicodePageLocation(page));
    }

    private float renderUnicodeChar(char ch, boolean italic)
    {
        int i = this.glyphWidth[ch] & 255;

        if (i == 0)
        {
            return 0.0F;
        }
        else
        {
            int j = ch / 256;
            this.loadGlyphTexture(j);
            int k = i >>> 4;
            int l = i & 15;
            float f = k;
            float f1 = l + 1;
            float f2 = ch % 16 * 16 + f;
            float f3 = (ch & 255) / 16 * 16;
            float f4 = f1 - f - 0.02F;
            float f5 = italic ? 1.0F : 0.0F;
            GlStateManager.glBegin(5);
            GlStateManager.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(this.posX + f5, this.posY, 0.0F);
            GlStateManager.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(this.posX - f5, this.posY + 7.99F, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
            GlStateManager.glVertex3f(this.posX + f4 / 2.0F + f5, this.posY, 0.0F);
            GlStateManager.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
            GlStateManager.glVertex3f(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F);
            GlStateManager.glEnd();
            return (f1 - f) / 2.0F + 1.0F;
        }
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow)
    {
        this.enableAlpha();
        this.resetStyles();
        int i;

        if (dropShadow)
        {
            i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        }
        else
        {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }

    private void resetStyles()
    {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String text, boolean shadow)
    {
        for (int i = 0; i < text.length(); ++i)
        {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length())
            {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16)
                {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (i1 < 0 || i1 > 15)
                    {
                        i1 = 15;
                    }
                    if (shadow)
                    {
                        i1 += 16;
                    }
                    int j1 = this.colorCode[i1];
                    this.setColor((j1 >> 16) / 255.0F, (j1 >> 8 & 255) / 255.0F, (j1 & 255) / 255.0F, this.alpha);
                }
                else if (i1 == 16)
                {
                    this.randomStyle = true;
                }
                else if (i1 == 17)
                {
                    this.boldStyle = true;
                }
                else if (i1 == 18)
                {
                    this.strikethroughStyle = true;
                }
                else if (i1 == 19)
                {
                    this.underlineStyle = true;
                }
                else if (i1 == 20)
                {
                    this.italicStyle = true;
                }
                else if (i1 == 21)
                {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
            }
            else
            {
                int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

                if (this.randomStyle && j != -1)
                {
                    int k = this.getCharWidth(c0);
                    char c1;

                    while (true)
                    {
                        j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                        c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

                        if (k == this.getCharWidth(c1))
                        {
                            break;
                        }
                    }
                    c0 = c1;
                }

                float f1 = 0.5F;
                boolean flag = (c0 == 0 || j == -1) && shadow;

                if (flag)
                {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, this.italicStyle);

                if (flag)
                {
                    this.posX += f1;
                    this.posY += f1;
                }
                if (this.boldStyle)
                {
                    this.posX += f1;

                    if (flag)
                    {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderChar(c0, this.italicStyle);
                    this.posX -= f1;

                    if (flag)
                    {
                        this.posX += f1;
                        this.posY += f1;
                    }
                    ++f;
                }
                this.doDraw(f);
            }
        }
    }

    private void doDraw(float f)
    {
        if (this.strikethroughStyle)
        {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0D).endVertex();
            vertexbuffer.pos(this.posX + f, this.posY + this.FONT_HEIGHT / 2, 0.0D).endVertex();
            vertexbuffer.pos(this.posX + f, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
            vertexbuffer.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        if (this.underlineStyle)
        {
            Tessellator tessellator1 = Tessellator.getInstance();
            BufferBuilder vertexbuffer1 = tessellator1.getBuffer();
            GlStateManager.disableTexture2D();
            vertexbuffer1.begin(7, DefaultVertexFormats.POSITION);
            int l = this.underlineStyle ? -1 : 0;
            vertexbuffer1.pos(this.posX + l, this.posY + this.FONT_HEIGHT, 0.0D).endVertex();
            vertexbuffer1.pos(this.posX + f, this.posY + this.FONT_HEIGHT, 0.0D).endVertex();
            vertexbuffer1.pos(this.posX + f, this.posY + this.FONT_HEIGHT - 1.0F, 0.0D).endVertex();
            vertexbuffer1.pos(this.posX + l, this.posY + this.FONT_HEIGHT - 1.0F, 0.0D).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }
        this.posX += (int)f;
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow)
    {
        if (text == null)
        {
            return 0;
        }
        else
        {
            if ((color & -67108864) == 0)
            {
                color |= -16777216;
            }
            if (dropShadow)
            {
                color = (color & 16579836) >> 2 | color & -16777216;
            }
            this.red = (color >> 16 & 255) / 255.0F;
            this.blue = (color >> 8 & 255) / 255.0F;
            this.green = (color & 255) / 255.0F;
            this.alpha = (color >> 24 & 255) / 255.0F;
            this.setColor(this.red, this.blue, this.green, this.alpha);
            this.posX = x;
            this.posY = y;
            this.renderStringAtPos(text, dropShadow);
            return (int)this.posX;
        }
    }

    public int getStringWidth(String text)
    {
        if (text == null)
        {
            return 0;
        }
        else
        {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j)
            {
                char c0 = text.charAt(j);
                int k = this.getCharWidth(c0);

                if (k < 0 && j < text.length() - 1)
                {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 108 && c0 != 76)
                    {
                        if (c0 == 114 || c0 == 82)
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = true;
                    }
                    k = 0;
                }

                i += k;

                if (flag && k > 0)
                {
                    ++i;
                }
            }
            return i;
        }
    }

    private int getCharWidth(char character)
    {
        if (character == 167)
        {
            return -1;
        }
        else if (character == 32)
        {
            return 4;
        }
        else
        {
            if (this.glyphWidth[character] != 0)
            {
                int j = this.glyphWidth[character] & 255;
                int k = j >>> 4;
            int l = j & 15;
            ++l;
            return (l - k) / 2 + 1;
            }
            else
            {
                return 0;
            }
        }
    }

    private void setColor(float r, float g, float b, float a)
    {
        GlStateManager.color(r, g, b, a);
    }

    private void enableAlpha()
    {
        GlStateManager.enableAlpha();
    }

    private void bindTexture(ResourceLocation location)
    {
        IndicatiaMod.MC.getTextureManager().bindTexture(location);
    }

    private IResource getResource(ResourceLocation location) throws IOException
    {
        return IndicatiaMod.MC.getResourceManager().getResource(location);
    }
}