package stevekung.mods.indicatia.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;

public class CapeUtils
{
    public static final Map<String, DynamicTexture> CAPE_TEXTURE = new HashMap<>();
    public static final File pngFile = new File(Minecraft.getMinecraft().mcDataDir, "custom_cape");
    public static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtils.CAPE_TEXTURE.get(GameProfileUtils.getUsername()) != null)
        {
            GlStateManager.bindTexture(CapeUtils.CAPE_TEXTURE.get(GameProfileUtils.getUsername()).getGlTextureId());
        }
    }

    public static void loadCapeTexture()
    {
        if (!CapeUtils.textureDownloaded)
        {
            if (CapeUtils.pngFile.exists())
            {
                try
                {
                    CapeUtils.CAPE_TEXTURE.put(GameProfileUtils.getUsername(), new DynamicTexture(ImageIO.read(CapeUtils.pngFile)));
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
        if (CapeUtils.pngFile.exists())
        {
            try
            {
                CapeUtils.CAPE_TEXTURE.put(GameProfileUtils.getUsername(), new DynamicTexture(ImageIO.read(CapeUtils.pngFile)));
                ModLogger.info("Found downloaded custom cape file {}", CapeUtils.pngFile.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}