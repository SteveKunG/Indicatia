package stevekung.mods.indicatia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekungslib.utils.JsonUtils;

public class CapeUtils
{
    public static DynamicTexture CAPE_TEXTURE;
    public static final File texture = new File(ExtendedConfig.userDir, "custom_cape");
    static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        GlStateManager.bindTexture(Optional.ofNullable(CapeUtils.CAPE_TEXTURE.getGlTextureId()).orElse(null));
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
        NativeImage image = NativeImage.read(NativeImage.PixelFormat.RGBA, new FileInputStream(CapeUtils.texture));
        CapeUtils.CAPE_TEXTURE = new DynamicTexture(image);
    }
}