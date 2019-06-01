package stevekung.mods.indicatia.gui.overlay;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

@Environment(EnvType.CLIENT)
public class PlayerListHudNew extends PlayerListHud
{
    private static final Ordering<PlayerListEntry> ENTRY_ORDERING = Ordering.from(new EntryOrderComparator());
    private MinecraftClient client;
    private Component footer;
    private Component header;
    private long showTime;
    private boolean visible;

    public PlayerListHudNew()
    {
        super(MinecraftClient.getInstance(), MinecraftClient.getInstance().inGameHud);
        this.client = MinecraftClient.getInstance();
    }

    @Override
    protected void method_1923(int x1, int x2, int y, PlayerListEntry entry)
    {
        int ping = entry.getLatency();

        //if (IndicatiaConfig.GENERAL.enableCustomPlayerList.get())
        {
            ChatFormat color = ChatFormat.GREEN;

            if (ping >= 200 && ping < 300)
            {
                color = ChatFormat.YELLOW;
            }
            else if (ping >= 300 && ping < 500)
            {
                color = ChatFormat.RED;
            }
            else if (ping >= 500)
            {
                color = ChatFormat.DARK_RED;
            }
            String pingText = String.valueOf(ping);
            this.client.textRenderer.drawWithShadow(color + pingText, x1 + x2 - this.client.textRenderer.getStringWidth(pingText), y + 0.5F, 0);
        }
        /*else
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            byte index;

            if (entry.getLatency() < 0)
            {
                index = 5;
            }
            else if (entry.getLatency() < 150)
            {
                index = 0;
            }
            else if (entry.getLatency() < 300)
            {
                index = 1;
            }
            else if (entry.getLatency() < 600)
            {
                index = 2;
            }
            else if (entry.getLatency() < 1000)
            {
                index = 3;
            }
            else
            {
                index = 4;
            }
            this.blitOffset += 100;
            this.blit(x2 + x1 - 11, y, 0, 176 + index * 8, 10, 8);
            this.blitOffset -= 100;
        }*/
    }

    /** VANILLA CODE SECTION **/
    @Override
    public void tick(boolean run)
    {
        if (run && !this.visible)
        {
            this.showTime = SystemUtil.getMeasuringTimeMs();
        }
        this.visible = run;
    }

    @Override
    public void draw(int int_1, Scoreboard scoreboard_1, ScoreboardObjective scoreboardObjective_1)
    {
        ClientPlayNetworkHandler clientPlayNetworkHandler_1 = this.client.player.networkHandler;
        List<PlayerListEntry> list_1 = ENTRY_ORDERING.sortedCopy(clientPlayNetworkHandler_1.getPlayerList());
        int int_2 = 0;
        int int_3 = 0;
        Iterator<PlayerListEntry> var8 = list_1.iterator();
        int int_7;

        while (var8.hasNext())
        {
            PlayerListEntry playerListEntry_1 = var8.next();
            int_7 = this.client.textRenderer.getStringWidth(this.method_1918(playerListEntry_1).getFormattedText());
            int_2 = Math.max(int_2, int_7);

            if (scoreboardObjective_1 != null && scoreboardObjective_1.getRenderType() != ScoreboardCriterion.RenderType.HEARTS)
            {
                int_7 = this.client.textRenderer.getStringWidth(" " + scoreboard_1.getPlayerScore(playerListEntry_1.getProfile().getName(), scoreboardObjective_1).getScore());
                int_3 = Math.max(int_3, int_7);
            }
        }

        list_1 = list_1.subList(0, Math.min(list_1.size(), 80));
        int int_5 = list_1.size();
        int int_6 = int_5;

        for (int_7 = 1; int_6 > 20; int_6 = (int_5 + int_7 - 1) / int_7)
        {
            ++int_7;
        }

        boolean boolean_1 = this.client.isInSingleplayer() || this.client.getNetworkHandler().getClientConnection().isEncrypted();
        int int_10;

        if (scoreboardObjective_1 != null)
        {
            if (scoreboardObjective_1.getRenderType() == ScoreboardCriterion.RenderType.HEARTS)
            {
                int_10 = 90;
            }
            else
            {
                int_10 = int_3;
            }
        }
        else
        {
            int_10 = 0;
        }

        int int_11 = Math.min(int_7 * ((boolean_1 ? 9 : 0) + int_2 + int_10 + 13), int_1 - 50) / int_7;
        int int_12 = int_1 / 2 - (int_11 * int_7 + (int_7 - 1) * 5) / 2;
        int int_13 = 10;
        int int_14 = int_11 * int_7 + (int_7 - 1) * 5;
        List<String> list_2 = null;

        if (this.header != null)
        {
            list_2 = this.client.textRenderer.wrapStringToWidthAsList(this.header.getFormattedText(), int_1 - 50);
            String string_1;

            for(Iterator<String> var18 = list_2.iterator(); var18.hasNext(); int_14 = Math.max(int_14, this.client.textRenderer.getStringWidth(string_1)))
            {
                string_1 = var18.next();
            }
        }

        List<String> list_3 = null;
        String string_3;
        Iterator<String> var36;

        if (this.footer != null)
        {
            list_3 = this.client.textRenderer.wrapStringToWidthAsList(this.footer.getFormattedText(), int_1 - 50);

            for (var36 = list_3.iterator(); var36.hasNext(); int_14 = Math.max(int_14, this.client.textRenderer.getStringWidth(string_3)))
            {
                string_3 = var36.next();
            }
        }

        int var10000;
        int var10001;
        int var10002;
        int var10004;
        int int_18;

        if (list_2 != null)
        {
            var10000 = int_1 / 2 - int_14 / 2 - 1;
            var10001 = int_13 - 1;
            var10002 = int_1 / 2 + int_14 / 2 + 1;
            var10004 = list_2.size();
            this.client.textRenderer.getClass();
            fill(var10000, var10001, var10002, int_13 + var10004 * 9, Integer.MIN_VALUE);

            for (var36 = list_2.iterator(); var36.hasNext(); int_13 += 9)
            {
                string_3 = var36.next();
                int_18 = this.client.textRenderer.getStringWidth(string_3);
                this.client.textRenderer.drawWithShadow(string_3, int_1 / 2 - int_18 / 2, int_13, -1);
                this.client.textRenderer.getClass();
            }
            ++int_13;
        }

        fill(int_1 / 2 - int_14 / 2 - 1, int_13 - 1, int_1 / 2 + int_14 / 2 + 1, int_13 + int_6 * 9, Integer.MIN_VALUE);
        int int_16 = this.client.options.getTextBackgroundColor(553648127);
        int int_28;

        for (int int_17 = 0; int_17 < int_5; ++int_17)
        {
            int_18 = int_17 / int_6;
            int_28 = int_17 % int_6;
            int int_20 = int_12 + int_18 * int_11 + int_18 * 5;
            int int_21 = int_13 + int_28 * 9;
            fill(int_20, int_21, int_20 + int_11, int_21 + 8, int_16);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (int_17 < list_1.size())
            {
                PlayerListEntry playerListEntry_2 = list_1.get(int_17);
                GameProfile gameProfile_1 = playerListEntry_2.getProfile();
                int int_27;

                if (boolean_1)
                {
                    PlayerEntity playerEntity_1 = this.client.world.getPlayerByUuid(gameProfile_1.getId());
                    boolean boolean_2 = playerEntity_1 != null && playerEntity_1.isSkinOverlayVisible(PlayerModelPart.CAPE) && ("Dinnerbone".equals(gameProfile_1.getName()) || "Grumm".equals(gameProfile_1.getName()));
                    this.client.getTextureManager().bindTexture(playerListEntry_2.getSkinTexture());
                    int_27 = 8 + (boolean_2 ? 8 : 0);
                    int int_23 = 8 * (boolean_2 ? -1 : 1);
                    DrawableHelper.blit(int_20, int_21, 8, 8, 8.0F, int_27, 8, int_23, 64, 64);

                    if (playerEntity_1 != null && playerEntity_1.isSkinOverlayVisible(PlayerModelPart.HAT))
                    {
                        int int_24 = 8 + (boolean_2 ? 8 : 0);
                        int int_25 = 8 * (boolean_2 ? -1 : 1);
                        DrawableHelper.blit(int_20, int_21, 8, 8, 40.0F, int_24, 8, int_25, 64, 64);
                    }
                    int_20 += 9;
                }

                String string_4 = this.method_1918(playerListEntry_2).getFormattedText();

                if (playerListEntry_2.getGameMode() == GameMode.SPECTATOR)
                {
                    this.client.textRenderer.drawWithShadow(ChatFormat.ITALIC + string_4, int_20, int_21, -1862270977);
                }
                else
                {
                    this.client.textRenderer.drawWithShadow(string_4, int_20, int_21, -1);
                }

                if (scoreboardObjective_1 != null && playerListEntry_2.getGameMode() != GameMode.SPECTATOR)
                {
                    int int_26 = int_20 + int_2 + 1;
                    int_27 = int_26 + int_10;

                    if (int_27 - int_26 > 5)
                    {
                        this.method_1922(scoreboardObjective_1, int_21, gameProfile_1.getName(), int_26, int_27, playerListEntry_2);
                    }
                }
                this.method_1923(int_11, int_20 - (boolean_1 ? 9 : 0), int_21, playerListEntry_2);
            }
        }

        if (list_3 != null)
        {
            int_13 += int_6 * 9 + 1;
            var10000 = int_1 / 2 - int_14 / 2 - 1;
            var10001 = int_13 - 1;
            var10002 = int_1 / 2 + int_14 / 2 + 1;
            var10004 = list_3.size();
            this.client.textRenderer.getClass();
            fill(var10000, var10001, var10002, int_13 + var10004 * 9, Integer.MIN_VALUE);

            for(Iterator<String> var39 = list_3.iterator(); var39.hasNext(); int_13 += 9)
            {
                String string_5 = var39.next();
                int_28 = this.client.textRenderer.getStringWidth(string_5);
                this.client.textRenderer.drawWithShadow(string_5, int_1 / 2 - int_28 / 2, int_13, -1);
                this.client.textRenderer.getClass();
            }
        }
    }

    private void method_1922(ScoreboardObjective scoreboardObjective_1, int int_1, String string_1, int int_2, int int_3, PlayerListEntry playerListEntry_1)
    {
        int int_4 = scoreboardObjective_1.getScoreboard().getPlayerScore(string_1, scoreboardObjective_1).getScore();

        if (scoreboardObjective_1.getRenderType() == ScoreboardCriterion.RenderType.HEARTS)
        {
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            long long_1 = SystemUtil.getMeasuringTimeMs();

            if (this.showTime == playerListEntry_1.method_2976())
            {
                if (int_4 < playerListEntry_1.method_2973())
                {
                    playerListEntry_1.method_2978(long_1);
                    playerListEntry_1.method_2975(MinecraftClient.getInstance().inGameHud.getTicks() + 20);
                }
                else if (int_4 > playerListEntry_1.method_2973())
                {
                    playerListEntry_1.method_2978(long_1);
                    playerListEntry_1.method_2975(MinecraftClient.getInstance().inGameHud.getTicks() + 10);
                }
            }

            if (long_1 - playerListEntry_1.method_2974() > 1000L || this.showTime != playerListEntry_1.method_2976())
            {
                playerListEntry_1.method_2972(int_4);
                playerListEntry_1.method_2965(int_4);
                playerListEntry_1.method_2978(long_1);
            }

            playerListEntry_1.method_2964(this.showTime);
            playerListEntry_1.method_2972(int_4);
            int int_5 = MathHelper.ceil(Math.max(int_4, playerListEntry_1.method_2960()) / 2.0F);
            int int_6 = Math.max(MathHelper.ceil(int_4 / 2), Math.max(MathHelper.ceil(playerListEntry_1.method_2960() / 2), 10));
            boolean boolean_1 = playerListEntry_1.method_2961() > MinecraftClient.getInstance().inGameHud.getTicks() && (playerListEntry_1.method_2961() - MinecraftClient.getInstance().inGameHud.getTicks()) / 3L % 2L == 1L;

            if (int_5 > 0)
            {
                int int_7 = MathHelper.floor(Math.min((float)(int_3 - int_2 - 4) / (float)int_6, 9.0F));

                if (int_7 > 3)
                {
                    int int_9;

                    for (int_9 = int_5; int_9 < int_6; ++int_9)
                    {
                        this.blit(int_2 + int_9 * int_7, int_1, boolean_1 ? 25 : 16, 0, 9, 9);
                    }

                    for (int_9 = 0; int_9 < int_5; ++int_9)
                    {
                        this.blit(int_2 + int_9 * int_7, int_1, boolean_1 ? 25 : 16, 0, 9, 9);

                        if (boolean_1)
                        {
                            if (int_9 * 2 + 1 < playerListEntry_1.method_2960())
                            {
                                this.blit(int_2 + int_9 * int_7, int_1, 70, 0, 9, 9);
                            }
                            if (int_9 * 2 + 1 == playerListEntry_1.method_2960())
                            {
                                this.blit(int_2 + int_9 * int_7, int_1, 79, 0, 9, 9);
                            }
                        }

                        if (int_9 * 2 + 1 < int_4)
                        {
                            this.blit(int_2 + int_9 * int_7, int_1, int_9 >= 10 ? 160 : 52, 0, 9, 9);
                        }
                        if (int_9 * 2 + 1 == int_4)
                        {
                            this.blit(int_2 + int_9 * int_7, int_1, int_9 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else
                {
                    float float_1 = MathHelper.clamp(int_4 / 20.0F, 0.0F, 1.0F);
                    int int_10 = (int)((1.0F - float_1) * 255.0F) << 16 | (int)(float_1 * 255.0F) << 8;
                    String string_2 = "" + int_4 / 2.0F;

                    if (int_3 - this.client.textRenderer.getStringWidth(string_2 + "hp") >= int_2)
                    {
                        string_2 = string_2 + "hp";
                    }
                    this.client.textRenderer.drawWithShadow(string_2, (int_3 + int_2) / 2 - this.client.textRenderer.getStringWidth(string_2) / 2, int_1, int_10);
                }
            }
        }
        else
        {
            String string_3 = ChatFormat.YELLOW + "" + int_4;
            this.client.textRenderer.drawWithShadow(string_3, int_3 - this.client.textRenderer.getStringWidth(string_3), int_1, 16777215);
        }
    }

    @Override
    public void setFooter(Component component_1)
    {
        this.footer = component_1;
    }

    @Override
    public void setHeader(Component component_1)
    {
        this.header = component_1;
    }

    @Override
    public void clear()
    {
        this.header = null;
        this.footer = null;
    }

    @Environment(EnvType.CLIENT)
    static class EntryOrderComparator implements Comparator<PlayerListEntry>
    {
        private EntryOrderComparator() {}

        @Override
        public int compare(PlayerListEntry playerListEntry_1, PlayerListEntry playerListEntry_2)
        {
            Team team_1 = playerListEntry_1.getScoreboardTeam();
            Team team_2 = playerListEntry_2.getScoreboardTeam();
            return ComparisonChain.start().compareTrueFirst(playerListEntry_1.getGameMode() != GameMode.SPECTATOR, playerListEntry_2.getGameMode() != GameMode.SPECTATOR).compare(team_1 != null ? team_1.getName() : "", team_2 != null ? team_2.getName() : "").compare(playerListEntry_1.getProfile().getName(), playerListEntry_2.getProfile().getName(), String::compareToIgnoreCase).result();
        }
    }
}
