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
import net.minecraft.entity.EntityList;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
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

    public GuiNewChatUtil() {}

    public GuiNewChatUtil(String input)
    {
        super(input);
    }

    /*
        1 - Classic Games
        2 - Arcade
        3 - Build Battle
        4 - UHC Champions
        5 - Speed UHC
        6 - Crazy Walls
        7 - Skywars
        8 - Skywars Lab
        9 - Bedwars
        10 - Skyclash
        11 - TNT
        12 - Murder Mystery
        13 - Duels
        14 - Survival Games
        15 - Smash Heroes
        16 - Prototype: Zombies
        17 - Prototype: Hide and Seek
        18 - Prototype: Battle Royale
     */
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
            List<String> list = new ArrayList<>();

            list.add("Classic Games");
            list.add("Arcade");
            list.add("Build Battle");
            list.add("UHC Champions");
            list.add("Speed UHC");
            list.add("Crazy Walls");
            list.add("Skywars");
            list.add("Skywars Lab");
            list.add("Bedwars");
            list.add("Skyclash");
            list.add("TNT");
            list.add("Murder Mystery");
            list.add("Duels");
            list.add("Survival Games");
            list.add("Smash Heroes");
            list.add("Prototype: Zombies");
            list.add("Prototype: Hide and Seek");
            list.add("Prototype: Battle Royale");

            String[] array = list.toArray(new String[0]);

            // hypixel
            this.buttonList.add(new GuiButton(100, this.width - 63, enableCPS ? this.height - 56 : this.height - 35, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(101, this.width - 63, enableCPS ? this.height - 77 : this.height - 56, 60, 20, "Party Chat"));
            this.buttonList.add(new GuiButton(102, this.width - 63, enableCPS ? this.height - 98 : this.height - 77, 60, 20, "Guild Chat"));
            this.buttonList.add(this.lobbyOptions = new GuiDropdownElement(this, this.width - 150, 2, array));

            // skywars
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Skywars Lobby", "sw", "skywars", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo Normal", "solo_normal", "skywars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Solo Insane", "solo_insane", "skywars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Ranked Mode", "ranked_normal", "skywars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Team Normal", "teams_normal", "skywars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Team Insane", "teams_insane", "skywars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Mega Mode", "mega_normal", "skywars", true));

            // skywars labs
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Skywars Lobby", "sw", "skywars_lab", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo TNT", "solo_insane_tnt_madness", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Solo Rush", "solo_insane_rush", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Solo Slime", "solo_insane_slime", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Solo Lucky", "solo_insane_lucky", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Team TNT", "teams_insane_tnt_madness", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Team Rush", "teams_insane_rush", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 41, this, "Team Slime", "teams_insane_slime", "skywars_lab", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 41, this, "Team Lucky", "teams_insane_lucky", "skywars_lab", true));

            // bedwars
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Bedwars Lobby", "bw", "bedwars", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "bedwars_eight_one", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Doubles", "bedwars_eight_two", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "3v3v3v3", "bedwars_four_three", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "4v4v4v4", "bedwars_four_four", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Solo Capture", "bedwars_capture_solo", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Party Capture", "bedwars_capture_party", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 41, this, "4v4v4v4 Beta", "bedwars_four_four_beta", "bedwars", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 41, this, "Doubles Beta", "bedwars_eight_two_beta", "bedwars", true));

            // tnt
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "TNT Lobby", "tnt", "tnt", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "TNT Run", "tnt_tntrun", "tnt", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "PVP Run", "tnt_pvprun", "tnt", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Bow Spleef", "tnt_bowspleef", "tnt", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "TNT Tag", "tnt_tntag", "tnt", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Wizards", "tnt_capture", "tnt", true));

            // murder
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Murder Mystery Lobby", "mm", "murder", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Classic", "murder_classic", "murder", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Assassins", "murder_assassins", "murder", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Showdown", "murder_showdown", "murder", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Infection", "murder_infection", "murder", true));

            // skyclash
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Skyclash Lobby", "sc", "skyclash", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "skyclash_solo", "skyclash", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Doubles", "skyclash_doubles", "skyclash", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Team War", "skyclash_team_war", "skyclash", true));

            // uhc
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "UHC Lobby", "uhc", "uhc", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "uhc_solo", "uhc", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Teams", "uhc_teams", "uhc", true));

            // crazy walls
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Crazy Walls Lobby", "cw", "crazy_walls", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "crazy_walls_solo", "crazy_walls", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Teams", "crazy_walls_teams", "crazy_walls", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Solo Lucky", "crazy_walls_solo_chaos", "crazy_walls", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Teams Lucky", "crazy_walls_team_chaos", "crazy_walls", true));

            // survival game
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Blitz Surival Game Lobby", "sg", "survival_game", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "blitz_solo_normal", "survival_game", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Solo (No Kits)", "blitz_solo_nokits", "survival_game", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Team", "blitz_teams_normal", "survival_game", true));

            // speed uhc
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Speed UHC Lobby", "suhc", "speed_uhc", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "speed_solo_normal", "speed_uhc", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Solo Insane", "speed_solo_insane", "speed_uhc", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Team", "speed_team_normal", "speed_uhc", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Team Insane", "speed_team_insane", "speed_uhc", true));

            // duels
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Duels Lobby", "duels", "duels", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo Classic", "duels_classic_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Solo Skywars", "duels_sw_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Solo Bow", "duels_bow_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Solo UHC", "duels_uhc_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Solo No Debuffs", "duels_potion_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Solo Combo", "duels_combo_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 41, this, "Solo Potion", "duels_potion_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 41, this, "Solo OP", "duels_op_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 62, this, "Solo Mega Walls", "duels_mw_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 62, this, "Solo Blitz", "duels_blitz_duel", "duels", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 62, this, "Solo Bow Spleef", "duels_bowspleef_duel", "duels", true));

            // classic games
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Classic Lobby", "classic", "classic", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "VampireZ", "vampirez", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Paintball", "paintball", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Walls", "walls_normal", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Turbo Kart Racers", "tkr", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Quakecraft Solo Mode", "quake_solo", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Quakecraft Teams Mode", "quake_teams", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 41, this, "Arena 1v1 Mode", "arena_1v1", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 41, this, "Arena 2v2 Mode", "arena_2v2", "classic", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 62, this, "Arena 4v4 Mode", "arena_4v4", "classic", true));

            // build battle
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Build Battle Lobby", "bb", "build_battle", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "build_battle_solo_normal", "build_battle", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Teams", "build_battle_teams_normal", "build_battle", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Pro", "build_battle_solo_pro", "build_battle", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Guess the Build", "build_battle_guess_the_build", "build_battle", true));

            // arcade
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Arcade Lobby", "arcade", "arcade", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Mini Walls", "arcade_mini_walls", "arcade", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Football", "arcade_soccer", "arcade", true));

            // smash heroes
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Smash Heroes Lobby", "sh", "smash_heroes", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo 1v1v1v1", "super_smash_solo_normal", "smash_heroes", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Teams 2v2", "super_smash_2v2_normal", "smash_heroes", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Teams 2v2v2", "super_smash_teams_normal", "smash_heroes", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "1v1 Mode", "super_smash_1v1_normal", "smash_heroes", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Friends 1v1v1v1", "super_smash_friends_normal", "smash_heroes", true));

            // prototype zombies
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Prototype Lobby", "ptl", "prototype_zombie", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Story Normal", "prototype_zombies_story_normal", "prototype_zombie", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Story Hard", "prototype_zombies_story_hard", "prototype_zombie", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Story RIP", "prototype_zombies_story_rip", "prototype_zombie", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 36, 20, this, "Endless Normal", "prototype_zombies_endless_normal", "prototype_zombie", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 41, this, "Endless Hard", "prototype_zombies_endless_hard", "prototype_zombie", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 41, this, "Endless RIP", "prototype_zombies_endless_rip", "prototype_zombie", true));

            // prototype hide and seek
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Prototype Lobby", "ptl", "prototype_hide_and_seek", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Prop Hunt", "prototype_hide_and_seek_party_pooper", "prototype_hide_and_seek", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Party Pooper", "prototype_hide_and_seek_prop_hunt", "prototype_hide_and_seek", true));

            // prototype battle royale
            this.buttonList.add(new GuiButtonCustomize(this.width - 120, 20, this, "Prototype Lobby", "ptl", "prototype_battle_royale", false));
            this.buttonList.add(new GuiButtonCustomize(this.width - 99, 20, this, "Solo", "prototype_royale:solo", "prototype_battle_royale", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 78, 20, this, "Doubles", "prototype_royale:doubles", "prototype_battle_royale", true));
            this.buttonList.add(new GuiButtonCustomize(this.width - 57, 20, this, "Squad", "prototype_royale:squad", "prototype_battle_royale", true));

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

            for (GuiButton button : this.buttonList)
            {
                if (button instanceof GuiButtonCustomize)
                {
                    GuiButtonCustomize buttonCustom = (GuiButtonCustomize) button;

                    switch (ExtendedConfig.PREV_SELECT_DROPDOWN)
                    {
                    case 0:
                        if (buttonCustom.group.equalsIgnoreCase("classic"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 1:
                        if (buttonCustom.group.equalsIgnoreCase("arcade"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 2:
                        if (buttonCustom.group.equalsIgnoreCase("build_battle"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 3:
                        if (buttonCustom.group.equalsIgnoreCase("uhc"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 4:
                        if (buttonCustom.group.equalsIgnoreCase("speed_uhc"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 5:
                        if (buttonCustom.group.equalsIgnoreCase("crazy_walls"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 6:
                        if (buttonCustom.group.equalsIgnoreCase("skywars"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 7:
                        if (buttonCustom.group.equalsIgnoreCase("skywars_lab"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 8:
                        if (buttonCustom.group.equalsIgnoreCase("bedwars"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 9:
                        if (buttonCustom.group.equalsIgnoreCase("skyclash"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 10:
                        if (buttonCustom.group.equalsIgnoreCase("tnt"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 11:
                        if (buttonCustom.group.equalsIgnoreCase("murder"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 12:
                        if (buttonCustom.group.equalsIgnoreCase("duels"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 13:
                        if (buttonCustom.group.equalsIgnoreCase("survival_game"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 14:
                        if (buttonCustom.group.equalsIgnoreCase("smash_heroes"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 15:
                        if (buttonCustom.group.equalsIgnoreCase("prototype_zombie"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 16:
                        if (buttonCustom.group.equalsIgnoreCase("prototype_hide_and_seek"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    case 17:
                        if (buttonCustom.group.equalsIgnoreCase("prototype_battle_royale"))
                        {
                            buttonCustom.visible = clicked;
                        }
                        break;
                    }
                }
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
                this.mc.thePlayer.sendChatMessage(buttomCustom.command);
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
            this.mc.thePlayer.sendChatMessage("/chat a");
            this.mc.thePlayer.addChatMessage(IndicatiaMod.json.text("Reset Hypixel Chat"));
            break;
        case 101:
            this.mc.thePlayer.sendChatMessage("/chat p");
            this.mc.thePlayer.addChatMessage(IndicatiaMod.json.text("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            this.mc.thePlayer.sendChatMessage("/chat g");
            this.mc.thePlayer.addChatMessage(IndicatiaMod.json.text("Set chat mode to Hypixel Guild Chat"));
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
    protected void handleComponentHover(IChatComponent component, int mouseX, int mouseY)
    {
        if (component != null && component.getChatStyle().getChatHoverEvent() != null)
        {
            HoverEvent hover = component.getChatStyle().getChatHoverEvent();

            if (hover.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack itemStack = null;

                try
                {
                    NBTBase base = JsonToNBT.getTagFromJson(hover.getValue().getUnformattedText());

                    if (base instanceof NBTTagCompound)
                    {
                        itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound)base);
                    }
                }
                catch (NBTException e) {}

                if (itemStack != null)
                {
                    this.renderToolTip(itemStack, mouseX, mouseY);
                }
                else
                {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", mouseX, mouseY);
                }
            }
            else if (hover.getAction() == HoverEvent.Action.SHOW_ENTITY)
            {
                if (this.mc.gameSettings.advancedItemTooltips)
                {
                    try
                    {
                        NBTBase base = JsonToNBT.getTagFromJson(hover.getValue().getUnformattedText());

                        if (base instanceof NBTTagCompound)
                        {
                            List<String> list1 = new ArrayList<>();
                            NBTTagCompound compound = (NBTTagCompound)base;
                            String name = compound.getString("name");

                            for (String hide : HideNameData.getHideNameList())
                            {
                                if (name.contains(hide))
                                {
                                    name = name.replace(hide, EnumChatFormatting.OBFUSCATED + hide + EnumChatFormatting.RESET);
                                }
                            }

                            list1.add(name);

                            if (compound.hasKey("type", 8))
                            {
                                String s = compound.getString("type");
                                list1.add("Type: " + s + " (" + EntityList.getIDFromString(s) + ")");
                            }
                            list1.add(compound.getString("id"));
                            this.drawHoveringText(list1, mouseX, mouseY);
                        }
                        else
                        {
                            this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mouseX, mouseY);
                        }
                    }
                    catch (NBTException e)
                    {
                        this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", mouseX, mouseY);
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
                    IChatComponent itextcomponent = base.getStatName();
                    IChatComponent itextcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (base.isAchievement() ? "achievement" : "statistic"));
                    itextcomponent1.getChatStyle().setItalic(Boolean.valueOf(true));
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
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", mouseX, mouseY);
                }
            }
            GlStateManager.disableLighting();
        }
    }
}