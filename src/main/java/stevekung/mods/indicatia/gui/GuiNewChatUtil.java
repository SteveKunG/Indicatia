package stevekung.mods.indicatia.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Splitter;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.GuiDropdownElement.IDropboxCallback;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.util.HideNameData;
import stevekung.mods.indicatia.util.InfoUtil;

public class GuiNewChatUtil extends GuiChat implements IDropboxCallback
{
    private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;
    private GuiDropdownElement lobbyOptions;

    // skywars
    private GuiButton swLobby;
    private GuiButton swSoloNormal;
    private GuiButton swSoloInsane;
    private GuiButton swRankedMode;
    private GuiButton swTeamNormal;
    private GuiButton swTeamInsane;
    private GuiButton swMegaMode;

    // skywars labs
    private GuiButton swSoloTNT;
    private GuiButton swSoloRush;
    private GuiButton swSoloSlime;
    private GuiButton swSoloLucky;
    private GuiButton swTeamTNT;
    private GuiButton swTeamRush;
    private GuiButton swTeamSlime;
    private GuiButton swTeamLucky;

    // bedwars
    private GuiButton bwLobby;
    private GuiButton bwEightOne;
    private GuiButton bwEightTwo;
    private GuiButton bwFourThree;
    private GuiButton bwFourFour;

    // tnt
    private GuiButton tntLobby;
    private GuiButton tntRun;
    private GuiButton tntPvpRun;
    private GuiButton tntBowSpleef;
    private GuiButton tntTag;
    private GuiButton tntWizard;

    // murder
    private GuiButton mmLobby;
    private GuiButton mmClassic;
    private GuiButton mmAssassins;
    private GuiButton mmShowdown;
    private GuiButton mmInfection;
    private GuiButton mmHideAndSeek;

    // skyclash
    private GuiButton scLobby;
    private GuiButton scSolo;
    private GuiButton scDouble;
    private GuiButton scTeam;

    // uhc
    private GuiButton uhcLobby;
    private GuiButton uhcSolo;
    private GuiButton uhcTeam;

    // crazy walls
    private GuiButton cwLobby;
    private GuiButton cwSolo;
    private GuiButton cwTeam;

    // survival game
    private GuiButton sgLobby;
    private GuiButton sgSolo;
    private GuiButton sgSoloNoKit;
    private GuiButton sgTeam;

    // speed uhc
    private GuiButton suhcLobby;
    private GuiButton suhcSolo;
    private GuiButton suhcSoloInsane;
    private GuiButton suhcTeam;
    private GuiButton suhcTeamInsane;

    // prototype zombies
    private GuiButton ptlLobby;
    private GuiButton ptlStoryNormal;
    private GuiButton ptlStoryHard;
    private GuiButton ptlStoryRip;
    private GuiButton ptlEndlessNormal;
    private GuiButton ptlEndlessHard;
    private GuiButton ptlEndlessRip;

    // prototype duels
    private GuiButton ptlDuelClassic;
    private GuiButton ptlDuelBow;
    private GuiButton ptlDuelPotion;
    private GuiButton ptlDuelOP;
    private GuiButton ptlDuelMegawallsSolo;
    private GuiButton ptlDuelMegawallsDouble;
    private GuiButton ptlDuelMegawallsFour;
    private GuiButton ptlDuelUHCSolo;
    private GuiButton ptlDuelUHCDouble;
    private GuiButton ptlDuelUHCFour;

    public GuiNewChatUtil() {}

    public GuiNewChatUtil(String input)
    {
        super(input);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        boolean enableCPS = ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom");

        if (enableCPS)
        {
            this.buttonList.add(new GuiButton(0, this.width - 63, this.height - 35, 60, 20, "Reset CPS"));
            this.buttonList.add(new GuiCPSSlider(1, this.width - 165, this.height - 35, GuiCPSSlider.Options.CPS_OPACITY));
        }
        if (InfoUtil.INSTANCE.isHypixel())
        {
            // hypixel
            this.buttonList.add(new GuiButton(100, this.width - 63, enableCPS ? this.height - 56 : this.height - 35, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(101, this.width - 63, enableCPS ? this.height - 77 : this.height - 56, 60, 20, "Party Chat"));
            this.buttonList.add(new GuiButton(102, this.width - 63, enableCPS ? this.height - 98 : this.height - 77, 60, 20, "Guild Chat"));
            this.buttonList.add(this.lobbyOptions = new GuiDropdownElement(this, this.width - 123, 2, "Skywars", "Skywars Labs", "Bedwars", "TNT", "Murder Mystery", "Skyclash", "UHC Champions", "Crazy Walls", "Blitz Survival Game", "Speed UHC", "Prototype: Zombies", "Prototype: Duels"));

            // skywars
            this.buttonList.add(this.swLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Skywars Lobby"), "sw", false));
            this.buttonList.add(this.swSoloNormal = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo Normal"), "solo_normal", true));
            this.buttonList.add(this.swSoloInsane = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Solo Insane"), "solo_insane", true));
            this.buttonList.add(this.swRankedMode = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Ranked Mode"), "ranked_normal", true));
            this.buttonList.add(this.swTeamNormal = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("Team Normal"), "teams_normal", true));
            this.buttonList.add(this.swTeamInsane = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Team Insane"), "teams_insane", true));
            this.buttonList.add(this.swMegaMode = new GuiButtonCustomize(this.width - 78, 41, this, Arrays.asList("Mega Mode"), "mega_normal", true));

            // skywars labs
            this.buttonList.add(this.swSoloTNT = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo TNT"), "solo_insane_tnt_madness", true));
            this.buttonList.add(this.swSoloRush = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Solo Rush"), "solo_insane_rush", true));
            this.buttonList.add(this.swSoloSlime = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Solo Slime"), "solo_insane_slime", true));
            this.buttonList.add(this.swSoloLucky = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("Solo Lucky"), "solo_insane_lucky", true));
            this.buttonList.add(this.swTeamTNT = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Team TNT"), "teams_insane_tnt_madness", true));
            this.buttonList.add(this.swTeamRush = new GuiButtonCustomize(this.width - 78, 41, this, Arrays.asList("Team Rush"), "teams_insane_rush", true));
            this.buttonList.add(this.swTeamSlime = new GuiButtonCustomize(this.width - 57, 41, this, Arrays.asList("Team Slime"), "teams_insane_slime", true));
            this.buttonList.add(this.swTeamLucky = new GuiButtonCustomize(this.width - 36, 41, this, Arrays.asList("Team Lucky"), "teams_insane_lucky", true));

            // bedwars
            this.buttonList.add(this.bwLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Bedwars Lobby"), "bw", false));
            this.buttonList.add(this.bwEightOne = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "bedwars_eight_one", true));
            this.buttonList.add(this.bwEightTwo = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Doubles"), "bedwars_eight_two", true));
            this.buttonList.add(this.bwFourThree = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("3v3v3v3"), "bedwars_four_three", true));
            this.buttonList.add(this.bwFourFour = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("4v4v4v4"), "bedwars_four_four", true));

            // tnt
            this.buttonList.add(this.tntLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("TNT Lobby"), "tnt", false));
            this.buttonList.add(this.tntRun = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("TNT Run"), "tnt_tntrun", true));
            this.buttonList.add(this.tntPvpRun = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("PVP Run"), "tnt_pvprun", true));
            this.buttonList.add(this.tntBowSpleef = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Bow Spleef"), "tnt_bowspleef", true));
            this.buttonList.add(this.tntTag = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("TNT Tag"), "tnt_tntag", true));
            this.buttonList.add(this.tntWizard = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Wizards"), "tnt_capture", true));

            // murder
            this.buttonList.add(this.mmLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Murder Mystery Lobby"), "mm", false));
            this.buttonList.add(this.mmClassic = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Classic"), "murder_classic", true));
            this.buttonList.add(this.mmAssassins = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Assassins"), "murder_assassins", true));
            this.buttonList.add(this.mmShowdown = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Showdown"), "murder_showdown", true));
            this.buttonList.add(this.mmInfection = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("Infection"), "murder_infection", true));
            this.buttonList.add(this.mmHideAndSeek = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Hide And Seek"), "murder_hide_and_seek", true));

            // skyclash
            this.buttonList.add(this.scLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Skyclash Lobby"), "sc", false));
            this.buttonList.add(this.scSolo = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "skyclash_solo", true));
            this.buttonList.add(this.scDouble = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Doubles"), "skyclash_doubles", true));
            this.buttonList.add(this.scTeam = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Team War"), "skyclash_team_war", true));

            // uhc
            this.buttonList.add(this.uhcLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("UHC Lobby"), "uhc", false));
            this.buttonList.add(this.uhcSolo = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "uhc_solo", true));
            this.buttonList.add(this.uhcTeam = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Teams"), "uhc_teams", true));

            // crazy walls
            this.buttonList.add(this.cwLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Crazy Walls Lobby"), "cw", false));
            this.buttonList.add(this.cwSolo = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "crazy_walls_solo", true));
            this.buttonList.add(this.cwTeam = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Teams"), "crazy_walls_teams", true));

            // survival game
            this.buttonList.add(this.sgLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Blitz Surival Game Lobby"), "sg", false));
            this.buttonList.add(this.sgSolo = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "blitz_solo_normal", true));
            this.buttonList.add(this.sgSoloNoKit = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Solo (No Kits)"), "blitz_solo_nokits", true));
            this.buttonList.add(this.sgTeam = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Team"), "blitz_teams_normal", true));

            // speed uhc
            this.buttonList.add(this.suhcLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Speed UHC Lobby"), "suhc", false));
            this.buttonList.add(this.suhcSolo = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Solo"), "speed_solo_normal", true));
            this.buttonList.add(this.suhcSoloInsane = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Solo Insane"), "speed_solo_insane", true));
            this.buttonList.add(this.suhcTeam = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Team"), "speed_team_normal", true));
            this.buttonList.add(this.suhcTeamInsane = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("Team Insane"), "speed_team_insane", true));

            // prototype zombies
            this.buttonList.add(this.ptlLobby = new GuiButtonCustomize(this.width - 120, 20, this, Arrays.asList("Prototype Lobby"), "ptl", false));
            this.buttonList.add(this.ptlStoryNormal = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Story Normal"), "prototype_zombies_story_normal", true));
            this.buttonList.add(this.ptlStoryHard = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Story Hard"), "prototype_zombies_story_hard", true));
            this.buttonList.add(this.ptlStoryRip = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Story RIP"), "prototype_zombies_story_rip", true));
            this.buttonList.add(this.ptlEndlessNormal = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("Endless Normal"), "prototype_zombies_endless_normal", true));
            this.buttonList.add(this.ptlEndlessHard = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Endless Hard"), "prototype_zombies_endless_hard", true));
            this.buttonList.add(this.ptlEndlessRip = new GuiButtonCustomize(this.width - 78, 41, this, Arrays.asList("Endless RIP"), "prototype_zombies_endless_rip", true));

            // prototype duels
            this.buttonList.add(this.ptlDuelClassic = new GuiButtonCustomize(this.width - 99, 20, this, Arrays.asList("Classic"), "prototype_duels:classic_duel", true));
            this.buttonList.add(this.ptlDuelBow = new GuiButtonCustomize(this.width - 78, 20, this, Arrays.asList("Bow"), "prototype_duels:bow_duel", true));
            this.buttonList.add(this.ptlDuelPotion = new GuiButtonCustomize(this.width - 57, 20, this, Arrays.asList("Potion"), "prototype_duels:potion_duel", true));
            this.buttonList.add(this.ptlDuelOP = new GuiButtonCustomize(this.width - 36, 20, this, Arrays.asList("OP"), "prototype_duels:op_duel", true));
            this.buttonList.add(this.ptlDuelMegawallsSolo = new GuiButtonCustomize(this.width - 99, 41, this, Arrays.asList("Megawalls 1v1"), "prototype_duels:mw_duel", true));
            this.buttonList.add(this.ptlDuelMegawallsDouble = new GuiButtonCustomize(this.width - 78, 41, this, Arrays.asList("Megawalls 2v2"), "prototype_duels:mw_doubles", true));
            this.buttonList.add(this.ptlDuelMegawallsFour = new GuiButtonCustomize(this.width - 57, 41, this, Arrays.asList("Megawalls 4v4"), "prototype_duels:mw_four", true));
            this.buttonList.add(this.ptlDuelUHCSolo = new GuiButtonCustomize(this.width - 36, 41, this, Arrays.asList("UHC 1v1"), "prototype_duels:uhc_duel", true));
            this.buttonList.add(this.ptlDuelUHCDouble = new GuiButtonCustomize(this.width - 99, 62, this, Arrays.asList("UHC 2v2"), "prototype_duels:uhc_doubles", true));
            this.buttonList.add(this.ptlDuelUHCFour = new GuiButtonCustomize(this.width - 78, 62, this, Arrays.asList("UHC 4v4"), "prototype_duels:uhc_four", true));

            for (GuiButton button : this.buttonList)
            {
                if (!button.getClass().equals(GuiDropdownElement.class) && !(button.id >= 0 && button.id <= 102))
                {
                    button.visible = false;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (GuiButton button : this.buttonList)
        {
            if (button instanceof GuiButtonCustomize)
            {
                GuiButtonCustomize customButton = (GuiButtonCustomize) button;
                customButton.drawRegion(mouseX, mouseY);
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (InfoUtil.INSTANCE.isHypixel())
        {
            boolean clicked = !this.lobbyOptions.dropdownClicked;

            switch (ExtendedConfig.PREV_SELECT_DROPDOWN)
            {
            case 0:
                this.swLobby.visible = clicked;
                this.swSoloNormal.visible = clicked;
                this.swSoloInsane.visible = clicked;
                this.swRankedMode.visible = clicked;
                this.swTeamNormal.visible = clicked;
                this.swTeamInsane.visible = clicked;
                this.swMegaMode.visible = clicked;
                break;
            case 1:
                this.swLobby.visible = clicked;
                this.swSoloTNT.visible = clicked;
                this.swSoloRush.visible = clicked;
                this.swSoloSlime.visible = clicked;
                this.swSoloLucky.visible = clicked;
                this.swTeamTNT.visible = clicked;
                this.swTeamRush.visible = clicked;
                this.swTeamSlime.visible = clicked;
                this.swTeamLucky.visible = clicked;
                break;
            case 2:
                this.bwLobby.visible = clicked;
                this.bwEightOne.visible = clicked;
                this.bwEightTwo.visible = clicked;
                this.bwFourThree.visible = clicked;
                this.bwFourFour.visible = clicked;
                break;
            case 3:
                this.tntLobby.visible = clicked;
                this.tntRun.visible = clicked;
                this.tntPvpRun.visible = clicked;
                this.tntBowSpleef.visible = clicked;
                this.tntTag.visible = clicked;
                this.tntWizard.visible = clicked;
                break;
            case 4:
                this.mmLobby.visible = clicked;
                this.mmClassic.visible = clicked;
                this.mmAssassins.visible = clicked;
                this.mmShowdown.visible = clicked;
                this.mmInfection.visible = clicked;
                this.mmHideAndSeek.visible = clicked;
                break;
            case 5:
                this.scLobby.visible = clicked;
                this.scSolo.visible = clicked;
                this.scDouble.visible = clicked;
                this.scTeam.visible = clicked;
                break;
            case 6:
                this.uhcLobby.visible = clicked;
                this.uhcSolo.visible = clicked;
                this.uhcTeam.visible = clicked;
                break;
            case 7:
                this.cwLobby.visible = clicked;
                this.cwSolo.visible = clicked;
                this.cwTeam.visible = clicked;
                break;
            case 8:
                this.sgLobby.visible = clicked;
                this.sgSolo.visible = clicked;
                this.sgSoloNoKit.visible = clicked;
                this.sgTeam.visible = clicked;
                break;
            case 9:
                this.suhcLobby.visible = clicked;
                this.suhcSolo.visible = clicked;
                this.suhcSoloInsane.visible = clicked;
                this.suhcTeam.visible = clicked;
                this.suhcTeamInsane.visible = clicked;
                break;
            case 10:
                this.ptlLobby.visible = clicked;
                this.ptlStoryNormal.visible = clicked;
                this.ptlStoryHard.visible = clicked;
                this.ptlStoryRip.visible = clicked;
                this.ptlEndlessNormal.visible = clicked;
                this.ptlEndlessHard.visible = clicked;
                this.ptlEndlessRip.visible = clicked;
                break;
            case 11:
                this.ptlLobby.visible = clicked;
                this.ptlDuelClassic.visible = clicked;
                this.ptlDuelBow.visible = clicked;
                this.ptlDuelPotion.visible = clicked;
                this.ptlDuelOP.visible = clicked;
                this.ptlDuelMegawallsSolo.visible = clicked;
                this.ptlDuelMegawallsDouble.visible = clicked;
                this.ptlDuelMegawallsFour.visible = clicked;
                this.ptlDuelUHCSolo.visible = clicked;
                this.ptlDuelUHCDouble.visible = clicked;
                this.ptlDuelUHCFour.visible = clicked;
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String text = this.inputField.getText().trim();

            if (!text.isEmpty())
            {
                this.sendChatMessage(text);
            }
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            String space = ConfigManager.enableRCPS ? " " : "";
            int minX = ExtendedConfig.CPS_X_OFFSET;
            int minY = ExtendedConfig.CPS_Y_OFFSET;
            int maxX = ExtendedConfig.CPS_X_OFFSET + this.fontRendererObj.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
            int maxY = ExtendedConfig.CPS_Y_OFFSET + 12;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY)
            {
                this.isDragging = true;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
                ExtendedConfig.save();
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (this.isDragging)
            {
                ExtendedConfig.CPS_X_OFFSET += mouseX - this.lastPosX;
                ExtendedConfig.CPS_Y_OFFSET += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button instanceof GuiButtonCustomize)
        {
            GuiButtonCustomize buttomCustom = (GuiButtonCustomize) button;

            if (button.id == buttomCustom.id)
            {
                this.mc.player.sendChatMessage(buttomCustom.command);
            }
        }

        switch (button.id)
        {
        case 0:
            ScaledResolution res = new ScaledResolution(this.mc);
            ExtendedConfig.CPS_X_OFFSET = res.getScaledWidth() / 2 - 36;
            ExtendedConfig.CPS_Y_OFFSET = res.getScaledHeight() / 2 - 5;
            ExtendedConfig.save();
            break;
        case 100:
            this.mc.player.sendChatMessage("/chat a");
            this.mc.player.sendMessage(IndicatiaMod.json.text("Reset Hypixel Chat"));
            break;
        case 101:
            this.mc.player.sendChatMessage("/chat p");
            this.mc.player.sendMessage(IndicatiaMod.json.text("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            this.mc.player.sendChatMessage("/chat g");
            this.mc.player.sendMessage(IndicatiaMod.json.text("Set chat mode to Hypixel Guild Chat"));
            break;
        }
    }

    @Override
    public void onSelectionChanged(GuiDropdownElement dropdown, int selection)
    {
        ExtendedConfig.PREV_SELECT_DROPDOWN = selection;
        ExtendedConfig.save();
    }

    @Override
    public int getInitialSelection(GuiDropdownElement dropdown)
    {
        return ExtendedConfig.PREV_SELECT_DROPDOWN;
    }

    @Override
    protected void handleComponentHover(ITextComponent component, int mouseX, int mouseY)
    {
        if (component != null && component.getStyle().getHoverEvent() != null)
        {
            HoverEvent hover = component.getStyle().getHoverEvent();

            if (hover.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack itemStack = ItemStack.EMPTY;

                try
                {
                    NBTBase base = JsonToNBT.getTagFromJson(hover.getValue().getUnformattedText());

                    if (base instanceof NBTTagCompound)
                    {
                        itemStack = new ItemStack((NBTTagCompound)base);
                    }
                }
                catch (NBTException e) {}

                if (itemStack.isEmpty())
                {
                    this.drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Item!", mouseX, mouseY);
                }
                else
                {
                    this.renderToolTip(itemStack, mouseX, mouseY);
                }
            }
            else if (hover.getAction() == HoverEvent.Action.SHOW_ENTITY)
            {
                if (this.mc.gameSettings.advancedItemTooltips)
                {
                    try
                    {
                        NBTTagCompound compound = JsonToNBT.getTagFromJson(hover.getValue().getUnformattedText());
                        List<String> list1 = new ArrayList<>();
                        String name = compound.getString("name");

                        for (String hide : HideNameData.getHideNameList())
                        {
                            if (name.contains(hide))
                            {
                                name = name.replace(hide, TextFormatting.OBFUSCATED + hide + TextFormatting.RESET);
                            }
                        }

                        list1.add(name);

                        if (compound.hasKey("type", 8))
                        {
                            String s = compound.getString("type");
                            list1.add("Type: " + s);
                        }
                        list1.add(compound.getString("id"));
                        this.drawHoveringText(list1, mouseX, mouseY);
                    }
                    catch (NBTException e)
                    {
                        this.drawCreativeTabHoveringText(TextFormatting.RED + "Invalid Entity!", mouseX, mouseY);
                    }
                }
            }
            else if (hover.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                this.drawHoveringText(NEWLINE_SPLITTER.splitToList(hover.getValue().getFormattedText()), mouseX, mouseY);
            }
            else if (hover.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT)
            {
                StatBase base = StatList.getOneShotStat(hover.getValue().getUnformattedText());

                if (base != null)
                {
                    ITextComponent itextcomponent = base.getStatName();
                    ITextComponent itextcomponent1 = new TextComponentTranslation("stats.tooltip.type." + (base.isAchievement() ? "achievement" : "statistic"));
                    itextcomponent1.getStyle().setItalic(Boolean.valueOf(true));
                    String s1 = base instanceof Achievement ? ((Achievement)base).getDescription() : null;
                    List<String> list = new ArrayList<>(Arrays.asList(itextcomponent.getFormattedText(), itextcomponent1.getFormattedText()));

                    if (s1 != null)
                    {
                        list.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
                    }
                    this.drawHoveringText(list, mouseX, mouseY);
                }
                else
                {
                    this.drawCreativeTabHoveringText(TextFormatting.RED + "Invalid statistic/achievement!", mouseX, mouseY);
                }
            }
            GlStateManager.disableLighting();
        }
    }
}