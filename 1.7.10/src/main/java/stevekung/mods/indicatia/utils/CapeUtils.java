package stevekung.mods.indicatia.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class CapeUtils
{
    public static final Map<String, DynamicTexture> CAPE_TEXTURE = Maps.newHashMap();
    public static final File pngFile = new File(IndicatiaMod.MC.mcDataDir, "custom_cape");
    public static boolean textureDownloaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtils.CAPE_TEXTURE.get(GameProfileUtil.getUsername()) != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, CapeUtils.CAPE_TEXTURE.get(GameProfileUtil.getUsername()).getGlTextureId());
        }
    }

    public static void loadCapeTexture()
    {
        JsonUtil json = new JsonUtil();

        if (!CapeUtils.textureDownloaded)
        {
            if (CapeUtils.pngFile.exists())
            {
                try
                {
                    CapeUtils.CAPE_TEXTURE.put(GameProfileUtil.getUsername(), new DynamicTexture(ImageIO.read(CapeUtils.pngFile)));
                    IndicatiaMod.MC.thePlayer.addChatMessage(json.text("New custom cape texture successfully downloaded").setChatStyle(json.colorFromConfig("green")));
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
                CapeUtils.CAPE_TEXTURE.put(GameProfileUtil.getUsername(), new DynamicTexture(ImageIO.read(CapeUtils.pngFile)));
                ModLogger.info("Found downloaded custom cape file {}", CapeUtils.pngFile.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}