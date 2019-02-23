package stevekung.mods.indicatia.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;

public class CapeUtils
{
    public static DynamicTexture CAPE_TEXTURE;
    public static final File texture = new File(ExtendedConfig.userDir, "custom_cape");
    static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtils.CAPE_TEXTURE != null)
        {
            GlStateManager.bindTexture(CapeUtils.CAPE_TEXTURE.getGlTextureId());
        }
    }

    public static void loadCapeTexture()
    {
        if (!CapeUtils.textureDownloaded)
        {
            if (CapeUtils.texture.exists())
            {
                try
                {
                    CapeUtils.readCapeTexture();
                    Minecraft.getInstance().player.sendMessage(JsonUtils.create("New custom cape texture successfully downloaded").setStyle(JsonUtils.green()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            CapeUtils.textureDownloaded = true;
        }
    }

    public static void loadCapeTextureAtStartup()
    {
        if (CapeUtils.texture.exists())
        {
            try
            {
                CapeUtils.readCapeTexture();
                LoggerIN.info("Found downloaded custom cape file {}", CapeUtils.texture.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void readCapeTexture() throws IOException
    {
        BufferedImage image = ImageIO.read(CapeUtils.texture);
        byte[] buffer = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        InputStream inputStream = new ByteArrayInputStream(buffer);
        CapeUtils.CAPE_TEXTURE = new DynamicTexture(NativeImage.read(NativeImage.PixelFormat.RGBA, inputStream));
    }
}