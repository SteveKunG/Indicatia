package stevekung.mods.indicatia.utils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.JsonUtils;

public class CapeUtils
{
    public static DynamicTexture CAPE_TEXTURE;
    public static final File texture = new File(ExtendedConfig.userDir, "custom_cape");
    public static boolean textureDownloaded = true;

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
                    CapeUtils.CAPE_TEXTURE = new DynamicTexture(ImageIO.read(CapeUtils.texture));
                    Minecraft.getMinecraft().player.sendMessage(JsonUtils.create("New custom cape texture successfully downloaded").setStyle(JsonUtils.green()));
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
                CapeUtils.CAPE_TEXTURE = new DynamicTexture(ImageIO.read(CapeUtils.texture));
                LoggerIN.info("Found downloaded custom cape file {}", CapeUtils.texture.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}