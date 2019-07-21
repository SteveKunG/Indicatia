package stevekung.mods.indicatia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekungslib.utils.JsonUtils;

public class CapeUtils
{
    public static NativeImageBackedTexture CAPE_TEXTURE;
    public static final File texture = new File(ExtendedConfig.userDir, "custom_cape");
    static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtils.CAPE_TEXTURE != null)
        {
            GlStateManager.bindTexture(CapeUtils.CAPE_TEXTURE.getGlId());
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
                    MinecraftClient.getInstance().player.addChatMessage(JsonUtils.create("New custom cape texture successfully downloaded").setStyle(JsonUtils.green()), false);
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
                IndicatiaMod.LOGGER.info("Found downloaded custom cape file {}", CapeUtils.texture.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void readCapeTexture() throws IOException
    {
        NativeImage image = NativeImage.read(new FileInputStream(CapeUtils.texture));
        CapeUtils.CAPE_TEXTURE = new NativeImageBackedTexture(image);
    }
}