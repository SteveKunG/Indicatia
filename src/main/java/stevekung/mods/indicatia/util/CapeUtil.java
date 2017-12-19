package stevekung.mods.indicatia.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class CapeUtil
{
    public static final Map<String, DynamicTexture> CAPE_TEXTURE = new HashMap<>();
    public static final File pngFile = new File(IndicatiaMod.MC.mcDataDir, "custom_cape");
    public static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtil.CAPE_TEXTURE.get(GameProfileUtil.getUsername()) != null)
        {
            GlStateManager.bindTexture(CapeUtil.CAPE_TEXTURE.get(GameProfileUtil.getUsername()).getGlTextureId());
        }
    }

    public static void loadCapeTexture()
    {
        JsonUtil json = IndicatiaMod.json;

        if (!CapeUtil.textureDownloaded)
        {
            if (CapeUtil.pngFile.exists())
            {
                try
                {
                    CapeUtil.CAPE_TEXTURE.put(GameProfileUtil.getUsername(), new DynamicTexture(ImageIO.read(CapeUtil.pngFile)));
                    IndicatiaMod.MC.player.sendMessage(json.text("New custom cape texture successfully downloaded").setStyle(json.green()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            CapeUtil.textureDownloaded = true;
        }
    }

    public static void loadCapeTextureAtStartup()
    {
        if (CapeUtil.pngFile.exists())
        {
            try
            {
                CapeUtil.CAPE_TEXTURE.put(GameProfileUtil.getUsername(), new DynamicTexture(ImageIO.read(CapeUtil.pngFile)));
                ModLogger.info("Found downloaded custom cape file {}", CapeUtil.pngFile.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}