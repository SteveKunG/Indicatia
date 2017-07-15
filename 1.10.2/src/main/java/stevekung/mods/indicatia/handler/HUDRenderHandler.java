package stevekung.mods.indicatia.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiBossOverlayNew;
import stevekung.mods.indicatia.utils.HUDInfo;
import stevekung.mods.indicatia.utils.JsonUtil;
import stevekung.mods.indicatia.utils.RenderUtil;

public class HUDRenderHandler
{
    private final Minecraft mc;
    private GuiBossOverlayNew overlayBoss;
    public static boolean recordEnable;
    private int recTick;

    public HUDRenderHandler(Minecraft mc)
    {
        this.mc = mc;
        this.overlayBoss = new GuiBossOverlayNew();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (HUDRenderHandler.recordEnable)
            {
                this.recTick++;
            }
            else
            {
                this.recTick = 0;
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            if (ConfigManager.enableRenderInfo && !this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
            {
                List<String> list = new ArrayList<>();

                if (ConfigManager.enablePing && !this.mc.isSingleplayer())
                {
                    list.add(HUDInfo.getPing());
                }
                if (ConfigManager.enableFPS)
                {
                    list.add(HUDInfo.getFPS());
                }
                if (ConfigManager.enableXYZ)
                {
                    list.add(HUDInfo.getXYZ(this.mc));

                    if (this.mc.thePlayer.dimension == -1)
                    {
                        list.add(HUDInfo.getOverworldXYZFromNether(this.mc));
                    }
                }
                if (ConfigManager.enableBiome)
                {
                    list.add(HUDInfo.getBiome(this.mc));
                }
                if (ConfigManager.enableServerIP && this.mc.getCurrentServerData() != null && !this.mc.isSingleplayer())
                {
                    list.add(HUDInfo.getServerIP(this.mc));
                }
                if (ExtendedConfig.CPS_POSITION.equals("left"))
                {
                    if (ConfigManager.enableCPS)
                    {
                        list.add(HUDInfo.getCPS(this.mc));
                    }
                    if (ConfigManager.enableRCPS)
                    {
                        list.add(HUDInfo.getRCPS(this.mc));
                    }
                }

                if (!this.mc.thePlayer.isSpectator() && ConfigManager.enableRenderEquippedItem)
                {
                    HUDInfo.renderEquippedItems(this.mc);
                }

                for (int i = 0; i < list.size(); ++i)
                {
                    String string = list.get(i);
                    float fontHeight = this.mc.fontRendererObj.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;

                    if (!string.isEmpty())
                    {
                        this.mc.mcProfiler.startSection("indicatia_info");
                        this.mc.fontRendererObj.drawString(string, 3.0625F, yOffset, 16777215, true);
                        this.mc.mcProfiler.endSection();
                    }
                }
            }

            if (HUDRenderHandler.recordEnable)
            {
                ScaledResolution res = new ScaledResolution(this.mc);
                int color = 16777215;

                if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                {
                    color = 16733525;
                }
                this.mc.fontRendererObj.drawString("REC: " + StringUtils.ticksToElapsedTime(this.recTick), res.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("REC: " + StringUtils.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.PLAYER_LIST)
        {
            if (CommonHandler.overlayPlayerList != null)
            {
                event.setCanceled(true);
                ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
                NetHandlerPlayClient handler = this.mc.thePlayer.connection;

                if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
                {
                    CommonHandler.overlayPlayerList.updatePlayerList(true);
                    CommonHandler.overlayPlayerList.renderPlayerlist(event.getResolution().getScaledWidth(), this.mc.theWorld.getScoreboard(), scoreobjective);
                }
                else
                {
                    CommonHandler.overlayPlayerList.updatePlayerList(false);
                }
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
        {
            if (ConfigManager.enableChatDepthRender)
            {
                event.setCanceled(true);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, event.getResolution().getScaledHeight() - 48, 0.0F);
                GlStateManager.disableDepth();
                this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getUpdateCounter());
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            if (!ConfigManager.enableIngamePotionHUD)
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            this.mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableBlend();
            this.overlayBoss.renderBossHealth();
            GlStateManager.disableBlend();
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post event)
    {
        EntityLivingBase entity = event.getEntity();
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(this.mc.getRenderViewEntity());
        String mode = ConfigManager.healthStatusMode;
        boolean flag = true;
        String color = "green";
        JsonUtil json = new JsonUtil();

        if (halfHealth)
        {
            color = "red";
        }
        if (halfHealth1)
        {
            color = "dark_red";
        }

        if (mode.equals("disable"))
        {
            flag = false;
        }
        else if (mode.equals("pointed"))
        {
            flag = entity == this.mc.pointedEntity;
        }

        if (distance < range * range)
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof EntityPlayerSP || entity instanceof EntityArmorStand))
            {
                String heart = json.text("\u2764 ").setStyle(json.colorFromConfig(color)).getFormattedText();
                RenderUtil.renderEntityHealth(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ());
            }
        }
    }
}