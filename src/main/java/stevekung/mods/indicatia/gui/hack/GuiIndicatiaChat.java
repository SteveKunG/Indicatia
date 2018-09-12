package stevekung.mods.indicatia.gui.hack;

import java.io.IOException;
import java.util.*;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiButtonCustomize;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames.IDropboxCallback;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.indicatia.utils.HypixelMinigameGroup;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.client.gui.IEntityHoverChat;
import stevekung.mods.stevekunglib.client.gui.IGuiChat;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiIndicatiaChat implements IGuiChat, IEntityHoverChat, IDropboxCallback
{
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;
    private GuiDropdownMinigames dropdown;
    private int prevSelect = -1;

    @Override
    public void initGui(List<GuiButton> buttonList, int width, int height)
    {
        this.updateButton(buttonList, width, height);
    }

    @Override
    public void drawScreen(List<GuiButton> buttonList, int mouseX, int mouseY, float partialTicks)
    {
        buttonList.forEach(button ->
        {
            if (button instanceof GuiButtonCustomize)
            {
                GuiButtonCustomize customButton = (GuiButtonCustomize) button;
                customButton.drawRegion(mouseX, mouseY);
            }
        });
    }

    @Override
    public void updateScreen(List<GuiButton> buttonList, int width, int height)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            if (this.prevSelect != ExtendedConfig.selectedHypixelMinigame)
            {
                this.updateButton(buttonList, width, height);
                this.prevSelect = ExtendedConfig.selectedHypixelMinigame;
            }

            boolean clicked = !this.dropdown.dropdownClicked;

            buttonList.forEach(button ->
            {
                if (button instanceof GuiButtonCustomize)
                {
                    GuiButtonCustomize buttonCustom = (GuiButtonCustomize) button;
                    buttonCustom.visible = clicked && ExtendedConfig.selectedHypixelMinigame == buttonCustom.group.ordinal();
                }
            });
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
        {
            String space = ExtendedConfig.rcps ? " " : "";
            int minX = ExtendedConfig.cpsCustomXOffset;
            int minY = ExtendedConfig.cpsCustomYOffset;
            int maxX = ExtendedConfig.cpsCustomXOffset + Minecraft.getMinecraft().fontRenderer.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
            int maxY = ExtendedConfig.cpsCustomYOffset + 12;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY)
            {
                this.isDragging = true;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
                ExtendedConfig.save();
            }
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
        {
            if (this.isDragging)
            {
                ExtendedConfig.cpsCustomXOffset += mouseX - this.lastPosX;
                ExtendedConfig.cpsCustomYOffset += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (button instanceof GuiButtonCustomize)
        {
            GuiButtonCustomize buttomCustom = (GuiButtonCustomize) button;

            if (button.id == buttomCustom.id)
            {
                player.sendChatMessage(buttomCustom.command);
            }
        }

        switch (button.id)
        {
        case 0:
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            ExtendedConfig.cpsCustomXOffset = res.getScaledWidth() / 2 - (ExtendedConfig.rcps ? 36 : 16);
            ExtendedConfig.cpsCustomYOffset = res.getScaledHeight() / 2 - 5;
            ExtendedConfig.save();
            break;
        case 100:
            player.sendChatMessage("/chat a");
            player.sendMessage(JsonUtils.create("Reset Hypixel Chat"));
            break;
        case 101:
            player.sendChatMessage("/chat p");
            player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            player.sendChatMessage("/chat g");
            player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Guild Chat"));
            break;
        }
    }

    @Override
    public String addEntityComponent(String name)
    {
        for (String hide : HideNameData.getHideNameList())
        {
            if (name.contains(hide))
            {
                name = name.replace(hide, TextFormatting.OBFUSCATED + hide + TextFormatting.RESET);
            }
        }
        return name;
    }

    @Override
    public void onGuiClosed()
    {
        ExtendedConfig.save();
    }

    @Override
    public void handleMouseInput(int width, int height) throws IOException
    {
        Minecraft mc = Minecraft.getMinecraft();
        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        if (this.dropdown != null && this.dropdown.dropdownClicked && this.dropdown.isHoverDropdown(mouseX, mouseY))
        {
            int i = Mouse.getEventDWheel();

            if (i != 0)
            {
                if (i > 1)
                {
                    i = -1;
                }
                if (i < -1)
                {
                    i = 1;
                }
                if (GuiScreen.isCtrlKeyDown())
                {
                    i *= 7;
                }
                this.dropdown.scroll(i);
            }
            if (Mouse.getEventButtonState())
            {
                // hacked mouse clicked :D
                int event = Mouse.getEventButton();
                this.mouseClicked(mouseX, mouseY, event);
            }
        }
    }

    @Override
    public void keyTypedScrollDown() {}

    @Override
    public void keyTypedScrollUp() {}

    @Override
    public void getSentHistory(int msgPos) {}

    @Override
    public void onSelectionChanged(GuiDropdownMinigames dropdown, int selection)
    {
        ExtendedConfig.selectedHypixelMinigame = selection;
        ExtendedConfig.save();
    }

    @Override
    public int getInitialSelection(GuiDropdownMinigames dropdown)
    {
        return ExtendedConfig.selectedHypixelMinigame;
    }

    private void updateButton(List<GuiButton> buttonList, int width, int height)
    {
        Minecraft mc = Minecraft.getMinecraft();
        buttonList.clear();
        boolean enableCPS = ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom");

        if (enableCPS)
        {
            buttonList.add(new GuiButton(0, width - 63, height - 35, 60, 20, LangUtils.translate("message.reset_cps")));
        }
        if (InfoUtils.INSTANCE.isHypixel())
        {
            List<String> list = new ArrayList<>();

            Arrays.asList(HypixelMinigameGroup.values).forEach(group ->
            {
                list.add(group.getName());
            });

            String max = Collections.max(list, Comparator.comparing(text -> text.length()));
            int length = mc.fontRenderer.getStringWidth(max) + 25;

            // hypixel chat
            buttonList.add(new GuiButton(100, width - 63, enableCPS ? height - 56 : height - 35, 60, 20, "Reset Chat"));
            buttonList.add(new GuiButton(101, width - 63, enableCPS ? height - 77 : height - 56, 60, 20, "Party Chat"));
            buttonList.add(new GuiButton(102, width - 63, enableCPS ? height - 98 : height - 77, 60, 20, "Guild Chat"));
            buttonList.add(this.dropdown = new GuiDropdownMinigames(this, width - length, 2, list));
            this.dropdown.width = length;
            this.prevSelect = ExtendedConfig.selectedHypixelMinigame;

            List<GuiButtonCustomize> gameBtn = new ArrayList<>();
            int xPos2 = width - 99;

            if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MAIN.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Main Lobby", "main", HypixelMinigameGroup.MAIN, false));
                gameBtn.add(new GuiButtonCustomize(width, "Housing", "/home", HypixelMinigameGroup.MAIN, false));
                gameBtn.add(new GuiButtonCustomize(width, "Limbo", "/achat \u00A7", HypixelMinigameGroup.MAIN, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.ARCADE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Arcade Lobby", "arcade", HypixelMinigameGroup.ARCADE, false));
                gameBtn.add(new GuiButtonCustomize(width, "Party Games 1", "arcade_party_games_1", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Party Games 2", "arcade_party_games_2", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Party Games 3", "arcade_party_games_3", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Mini Walls", "arcade_mini_walls", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Football", "arcade_soccer", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Hole in the Wall", "arcade_hole_in_the_wall", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Bounty Hunters", "arcade_bounty_hunters", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Pixel Painters", "arcade_pixel_painters", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Dragon Wars", "arcade_dragon_wars", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Ender Spleef", "arcade_ender_spleef", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Galaxy Wars", "arcade_starwars", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Throw Out", "arcade_throw_out", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Creeper Attack", "arcade_creeper_attack", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Farm Hunt", "arcade_farm_hunt", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Hypixel Says", "arcade_simon_says", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Blocking Dead", "arcade_day_one", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Zombies: Dead End", "arcade_zombies_dead_end", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Zombies: Bad Blood", "arcade_zombies_bad_blood", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Zombies: Alien Arcadium", "arcade_zombies_alien_arcadium", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "H&S: Prop Hunt", "arcade_hide_and_seek_prop_hunt", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(width, "H&S: Party Pooper", "arcade_hide_and_seek_party_pooper", HypixelMinigameGroup.ARCADE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.CLASSIC_GAMES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Classic Lobby", "classic", HypixelMinigameGroup.CLASSIC_GAMES, false));
                gameBtn.add(new GuiButtonCustomize(width, "VampireZ", "vampirez", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Paintball", "paintball", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "The Walls", "walls", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Turbo Kart Racers", "tkr", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Quakecraft Solo", "quake_solo", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Quakecraft Doubles", "quake_teams", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Arena 1v1", "arena_1v1", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Arena 2v2", "arena_2v2", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Arena 4v4", "arena_4v4", HypixelMinigameGroup.CLASSIC_GAMES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.BEDWARS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Bedwars Lobby", "bw", HypixelMinigameGroup.BEDWARS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "bedwars_eight_one", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "bedwars_eight_two", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "3v3v3v3", "bedwars_four_three", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "4v4v4v4", "bedwars_four_four", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Capture", "bedwars_capture_solo", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Party Capture", "bedwars_capture_party", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Castle", "bedwars_castle", HypixelMinigameGroup.BEDWARS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.BUILD_BATTLE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Build Battle Lobby", "bb", HypixelMinigameGroup.BUILD_BATTLE, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "build_battle_solo_normal", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "build_battle_teams_normal", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Pro", "build_battle_solo_pro", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Guess the Build", "build_battle_guess_the_build", HypixelMinigameGroup.BUILD_BATTLE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.COPS_AND_CRIMS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Cops and Crims Lobby", "cnc", HypixelMinigameGroup.COPS_AND_CRIMS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Defusal", "mcgo_normal", HypixelMinigameGroup.COPS_AND_CRIMS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Deathmatch", "mcgo_deathmatch", HypixelMinigameGroup.COPS_AND_CRIMS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Defusal Party", "mcgo_normal_party", HypixelMinigameGroup.COPS_AND_CRIMS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Deathmatch Party", "mcgo_deathmatch_party", HypixelMinigameGroup.COPS_AND_CRIMS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.CRAZY_WALLS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Crazy Walls Lobby", "cw", HypixelMinigameGroup.CRAZY_WALLS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "crazy_walls_solo", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "crazy_walls_team", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Lucky", "crazy_walls_solo_chaos", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Lucky", "crazy_walls_team_chaos", HypixelMinigameGroup.CRAZY_WALLS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.DUELS_SOLO.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Duels Lobby", "duels", HypixelMinigameGroup.DUELS_SOLO, false));
                gameBtn.add(new GuiButtonCustomize(width, "Classic", "duels_classic_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Skywars", "duels_sw_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Bow", "duels_bow_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "UHC", "duels_uhc_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "No Debuffs", "duels_potion_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Combo", "duels_combo_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Potion", "duels_potion_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "OP", "duels_op_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Mega Walls", "duels_mw_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Blitz", "duels_blitz_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Bow Spleef", "duels_bowspleef_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(width, "Sumo", "duels_sumo_duel", HypixelMinigameGroup.DUELS_SOLO, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.DUELS_DOUBLES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Duels Lobby", "duels", HypixelMinigameGroup.DUELS_DOUBLES, false));
                gameBtn.add(new GuiButtonCustomize(width, "Skywars", "duels_sw_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles UHC", "duels_uhc_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles UHC", "duels_uhc_four", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "OP", "duels_op_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Mega Walls", "duels_mw_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "UHC Tournament", "duels_uhc_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "SW Tournament", "duels_sw_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Sumo Tournament", "duels_sumo_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MEGA_WALLS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Mega Walls Lobby", "mw", HypixelMinigameGroup.MEGA_WALLS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Standard", "mw_standard", HypixelMinigameGroup.MEGA_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Face Off", "mw_face_off", HypixelMinigameGroup.MEGA_WALLS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MURDER_MYSTERY.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Murder Mystery Lobby", "mm", HypixelMinigameGroup.MURDER_MYSTERY, false));
                gameBtn.add(new GuiButtonCustomize(width, "Classic", "murder_classic", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(width, "Assassins", "murder_assassins", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(width, "Showdown", "murder_showdown", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(width, "Infection", "murder_infection", HypixelMinigameGroup.MURDER_MYSTERY, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYCLASH.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Skyclash Lobby", "sc", HypixelMinigameGroup.SKYCLASH, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "skyclash_solo", HypixelMinigameGroup.SKYCLASH, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "skyclash_doubles", HypixelMinigameGroup.SKYCLASH, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles War", "skyclash_team_war", HypixelMinigameGroup.SKYCLASH, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYWARS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Skywars Lobby", "sw", HypixelMinigameGroup.SKYWARS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Normal", "solo_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Insane", "solo_insane", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Ranked Mode", "ranked_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Normal", "teams_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Insane", "teams_insane", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Mega Doubles", "mega_doubles", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Hunters vs Beasts", "solo_insane_hunters_vs_beasts", HypixelMinigameGroup.SKYWARS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYWARS_LAB.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Skywars Lobby", "sw", HypixelMinigameGroup.SKYWARS_LAB, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo TNT", "solo_insane_tnt_madness", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Rush", "solo_insane_rush", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Slime", "solo_insane_slime", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Lucky", "solo_insane_lucky", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles TNT", "teams_insane_tnt_madness", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Rush", "teams_insane_rush", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Slime", "teams_insane_slime", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Lucky", "teams_insane_lucky", HypixelMinigameGroup.SKYWARS_LAB, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SMASH_HEROES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Smash Heroes Lobby", "sh", HypixelMinigameGroup.SMASH_HEROES, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo 1v1v1v1", "super_smash_solo_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles 2v2", "super_smash_2v2_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles 2v2v2", "super_smash_teams_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(width, "1v1 Mode", "super_smash_1v1_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Friends 1v1v1v1", "super_smash_friends_normal", HypixelMinigameGroup.SMASH_HEROES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SPEED_UHC.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Speed UHC Lobby", "suhc", HypixelMinigameGroup.SPEED_UHC, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "speed_solo_normal", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo Insane", "speed_solo_insane", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "speed_team_normal", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles Insane", "speed_team_insane", HypixelMinigameGroup.SPEED_UHC, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SURVIVAL_GAMES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Blitz Surival Game Lobby", "sg", HypixelMinigameGroup.SURVIVAL_GAMES, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "blitz_solo_normal", HypixelMinigameGroup.SURVIVAL_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Solo (No Kits)", "blitz_solo_nokits", HypixelMinigameGroup.SURVIVAL_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "blitz_teams_normal", HypixelMinigameGroup.SURVIVAL_GAMES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.TNT.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "TNT Lobby", "tnt", HypixelMinigameGroup.TNT, false));
                gameBtn.add(new GuiButtonCustomize(width, "TNT Run", "tnt_tntrun", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(width, "PvP Run", "tnt_pvprun", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(width, "Bow Spleef", "tnt_bowspleef", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(width, "TNT Tag", "tnt_tntag", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(width, "Wizards", "tnt_capture", HypixelMinigameGroup.TNT, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.UHC_CHAMPIONS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "UHC Champions Lobby", "uhc", HypixelMinigameGroup.UHC_CHAMPIONS, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "uhc_solo", HypixelMinigameGroup.UHC_CHAMPIONS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "uhc_teams", HypixelMinigameGroup.UHC_CHAMPIONS, true));
                gameBtn.add(new GuiButtonCustomize(width, "Event", "uhc_event", HypixelMinigameGroup.UHC_CHAMPIONS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.WARLORDS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Warlords Lobby", "wl", HypixelMinigameGroup.WARLORDS, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, false));
                gameBtn.add(new GuiButtonCustomize(width, "Solo", "prototype_royale:solo", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Doubles", "prototype_royale:doubles", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
                gameBtn.add(new GuiButtonCustomize(width, "Squad", "prototype_royale:squad", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_KOTH.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_KOTH, false));
                gameBtn.add(new GuiButtonCustomize(width, "6v6", "prototype_koth_two_team", HypixelMinigameGroup.PROTOTYPE_KOTH, true));
                gameBtn.add(new GuiButtonCustomize(width, "3v3v3v3", "prototype_koth_four_team", HypixelMinigameGroup.PROTOTYPE_KOTH, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(width, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, false));
                gameBtn.add(new GuiButtonCustomize(width, "1v1", "prototype_bridge_1v1", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(width, "2v2", "prototype_bridge_2v2", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(width, "4v4", "prototype_bridge_4v4", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(width, "2v2v2v2", "prototype_bridge_2v2v2v2", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(width, "3v3v3v3", "prototype_bridge_3v3v3v3", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
            }

            for (int i = 0; i < gameBtn.size(); i++)
            {
                GuiButtonCustomize button = gameBtn.get(i);

                if (i <= 6)
                {
                    button.x += 21 * i;
                }
                if (i >= 6 && i <= 11)
                {
                    button.x = xPos2 - 136;
                    button.x += 21 * i;
                    button.y = 41;
                }
                if (i >= 11 && i <= 15)
                {
                    button.x = xPos2 - 241;
                    button.x += 21 * i;
                    button.y = 62;
                }
                if (i >= 16 && i <= 21)
                {
                    button.x = xPos2 - 346;
                    button.x += 21 * i;
                    button.y = 83;
                }
                if (i >= 21)
                {
                    button.x = xPos2 - 451;
                    button.x += 21 * i;
                    button.y = 104;
                }
                buttonList.add(button);
            }
        }

        buttonList.forEach(button ->
        {
            if (!button.getClass().equals(GuiDropdownMinigames.class) && !(button.id >= 0 && button.id <= 102))
            {
                button.visible = false;
            }
        });
    }
}