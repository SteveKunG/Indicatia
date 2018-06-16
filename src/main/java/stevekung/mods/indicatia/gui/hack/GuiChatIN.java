package stevekung.mods.indicatia.gui.hack;

import java.io.IOException;
import java.util.*;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiButtonCustomize;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames.IDropboxCallback;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.indicatia.utils.HypixelMinigameGroup;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiChatIN extends GuiChat implements IDropboxCallback
{
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;
    private GuiDropdownMinigames dropdown;
    private int prevSelect = -1;

    public GuiChatIN() {}

    public GuiChatIN(String input)
    {
        super(input);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.updateButton();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.buttonList.forEach(button ->
        {
            if (button instanceof GuiButtonCustomize)
            {
                GuiButtonCustomize customButton = (GuiButtonCustomize) button;
                customButton.drawRegion(mouseX, mouseY);
            }
        });
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            if (this.prevSelect != ExtendedConfig.selectedHypixelMinigame)
            {
                this.updateButton();
                this.prevSelect = ExtendedConfig.selectedHypixelMinigame;
            }

            boolean clicked = !this.dropdown.dropdownClicked;

            this.buttonList.forEach(button ->
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen(null);
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
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
        {
            String space = ExtendedConfig.rcps ? " " : "";
            int minX = ExtendedConfig.cpsCustomXOffset;
            int minY = ExtendedConfig.cpsCustomYOffset;
            int maxX = ExtendedConfig.cpsCustomXOffset + this.fontRenderer.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
            int maxY = ExtendedConfig.cpsCustomYOffset + 12;

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
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
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
            ExtendedConfig.cpsCustomXOffset = res.getScaledWidth() / 2 - (ExtendedConfig.rcps ? 36 : 16);
            ExtendedConfig.cpsCustomYOffset = res.getScaledHeight() / 2 - 5;
            ExtendedConfig.save();
            break;
        case 100:
            this.mc.player.sendChatMessage("/chat a");
            this.mc.player.sendMessage(JsonUtils.create("Reset Hypixel Chat"));
            break;
        case 101:
            this.mc.player.sendChatMessage("/chat p");
            this.mc.player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            this.mc.player.sendChatMessage("/chat g");
            this.mc.player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Guild Chat"));
            break;
        }
    }

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
                    this.drawHoveringText(TextFormatting.RED + "Invalid Item!", mouseX, mouseY);
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
                        List<String> list = new ArrayList<>();
                        String name = compound.getString("name");

                        for (String hide : HideNameData.getHideNameList())
                        {
                            if (name.contains(hide))
                            {
                                name = name.replace(hide, TextFormatting.OBFUSCATED + hide + TextFormatting.RESET);
                            }
                        }

                        list.add(name);

                        if (compound.hasKey("type", 8))
                        {
                            String s = compound.getString("type");
                            list.add("Type: " + s);
                        }
                        list.add(compound.getString("id"));
                        this.drawHoveringText(list, mouseX, mouseY);
                    }
                    catch (NBTException e)
                    {
                        this.drawHoveringText(TextFormatting.RED + "Invalid Entity!", mouseX, mouseY);
                    }
                }
            }
            else if (hover.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth(hover.getValue().getFormattedText(), Math.max(this.width / 2, 200)), mouseX, mouseY);
            }
            GlStateManager.disableLighting();
        }
    }

    @Override
    public void onGuiClosed()
    {
        ExtendedConfig.save();
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

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
                if (isCtrlKeyDown())
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
        else
        {
            super.handleMouseInput();
        }
    }

    private void updateButton()
    {
        this.buttonList.clear();
        boolean enableCPS = ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom");

        if (enableCPS)
        {
            this.buttonList.add(new GuiButton(0, this.width - 63, this.height - 35, 60, 20, LangUtils.translate("message.reset_cps")));
        }
        if (InfoUtils.INSTANCE.isHypixel())
        {
            List<String> list = new ArrayList<>();

            Arrays.asList(HypixelMinigameGroup.values).forEach(group ->
            {
                list.add(group.getName());
            });

            String max = Collections.max(list, Comparator.comparing(text -> text.length()));
            int length = this.mc.fontRenderer.getStringWidth(max) + 25;

            // hypixel chat
            this.buttonList.add(new GuiButton(100, this.width - 63, enableCPS ? this.height - 56 : this.height - 35, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(101, this.width - 63, enableCPS ? this.height - 77 : this.height - 56, 60, 20, "Party Chat"));
            this.buttonList.add(new GuiButton(102, this.width - 63, enableCPS ? this.height - 98 : this.height - 77, 60, 20, "Guild Chat"));
            this.buttonList.add(this.dropdown = new GuiDropdownMinigames(this, this.width - length, 2, list));
            this.dropdown.width = length;
            this.prevSelect = ExtendedConfig.selectedHypixelMinigame;

            List<GuiButtonCustomize> gameBtn = new ArrayList<>();
            int xPos2 = this.width - 99;

            if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MAIN.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Main Lobby", "main", HypixelMinigameGroup.MAIN, false));
                gameBtn.add(new GuiButtonCustomize(this, "Housing", "/home", HypixelMinigameGroup.MAIN, false));
                gameBtn.add(new GuiButtonCustomize(this, "Limbo", "/achat \u00A7", HypixelMinigameGroup.MAIN, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.ARCADE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Arcade Lobby", "arcade", HypixelMinigameGroup.ARCADE, false));
                gameBtn.add(new GuiButtonCustomize(this, "Mini Walls", "arcade_mini_walls", HypixelMinigameGroup.ARCADE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Football", "arcade_soccer", HypixelMinigameGroup.ARCADE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.CLASSIC_GAMES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Classic Lobby", "classic", HypixelMinigameGroup.CLASSIC_GAMES, false));
                gameBtn.add(new GuiButtonCustomize(this, "VampireZ", "vampirez", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Paintball", "paintball", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "The Walls", "walls", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Turbo Kart Racers", "tkr", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Quakecraft Solo", "quake_solo", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Quakecraft Teams", "quake_teams", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Arena 1v1", "arena_1v1", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Arena 2v2", "arena_2v2", HypixelMinigameGroup.CLASSIC_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Arena 4v4", "arena_4v4", HypixelMinigameGroup.CLASSIC_GAMES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.BEDWARS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Bedwars Lobby", "bw", HypixelMinigameGroup.BEDWARS, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "bedwars_eight_one", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Doubles", "bedwars_eight_two", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "3v3v3v3", "bedwars_four_three", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "4v4v4v4", "bedwars_four_four", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Capture", "bedwars_capture_solo", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Party Capture", "bedwars_capture_party", HypixelMinigameGroup.BEDWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Castle", "bedwars_castle", HypixelMinigameGroup.BEDWARS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.BUILD_BATTLE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Build Battle Lobby", "bb", HypixelMinigameGroup.BUILD_BATTLE, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "build_battle_solo_normal", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams", "build_battle_teams_normal", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Pro", "build_battle_solo_pro", HypixelMinigameGroup.BUILD_BATTLE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Guess the Build", "build_battle_guess_the_build", HypixelMinigameGroup.BUILD_BATTLE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.COPS_AND_CRIMS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Cops and Crims Lobby", "cnc", HypixelMinigameGroup.COPS_AND_CRIMS, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.CRAZY_WALLS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Crazy Walls Lobby", "cw", HypixelMinigameGroup.CRAZY_WALLS, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "crazy_walls_solo", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams", "crazy_walls_team", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Lucky", "crazy_walls_solo_chaos", HypixelMinigameGroup.CRAZY_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams Lucky", "crazy_walls_team_chaos", HypixelMinigameGroup.CRAZY_WALLS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.DUELS_SOLO.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Duels Lobby", "duels", HypixelMinigameGroup.DUELS_SOLO, false));
                gameBtn.add(new GuiButtonCustomize(this, "Classic", "duels_classic_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Skywars", "duels_sw_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Bow", "duels_bow_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "UHC", "duels_uhc_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "No Debuffs", "duels_potion_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Combo", "duels_combo_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Potion", "duels_potion_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "OP", "duels_op_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Mega Walls", "duels_mw_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Blitz", "duels_blitz_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Bow Spleef", "duels_bowspleef_duel", HypixelMinigameGroup.DUELS_SOLO, true));
                gameBtn.add(new GuiButtonCustomize(this, "Sumo", "duels_sumo_duel", HypixelMinigameGroup.DUELS_SOLO, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.DUELS_DOUBLES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Duels Lobby", "duels", HypixelMinigameGroup.DUELS_DOUBLES, false));
                gameBtn.add(new GuiButtonCustomize(this, "Skywars", "duels_sw_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Doubles UHC", "duels_uhc_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams UHC", "duels_uhc_four", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "OP", "duels_op_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Doubles Mega Walls", "duels_mw_doubles", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "UHC Tournament", "duels_uhc_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "SW Tournament", "duels_sw_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Sumo Tournament", "duels_sumo_tournament", HypixelMinigameGroup.DUELS_DOUBLES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MEGA_WALLS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Mega Walls Lobby", "mw", HypixelMinigameGroup.MEGA_WALLS, false));
                gameBtn.add(new GuiButtonCustomize(this, "Standard", "mw_standard", HypixelMinigameGroup.MEGA_WALLS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Face Off", "mw_face_off", HypixelMinigameGroup.MEGA_WALLS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.MURDER_MYSTERY.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Murder Mystery Lobby", "mm", HypixelMinigameGroup.MURDER_MYSTERY, false));
                gameBtn.add(new GuiButtonCustomize(this, "Classic", "murder_classic", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(this, "Assassins", "murder_assassins", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(this, "Showdown", "murder_showdown", HypixelMinigameGroup.MURDER_MYSTERY, true));
                gameBtn.add(new GuiButtonCustomize(this, "Infection", "murder_infection", HypixelMinigameGroup.MURDER_MYSTERY, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYCLASH.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Skyclash Lobby", "sc", HypixelMinigameGroup.SKYCLASH, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "skyclash_solo", HypixelMinigameGroup.SKYCLASH, true));
                gameBtn.add(new GuiButtonCustomize(this, "Doubles", "skyclash_doubles", HypixelMinigameGroup.SKYCLASH, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team War", "skyclash_team_war", HypixelMinigameGroup.SKYCLASH, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYWARS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Skywars Lobby", "sw", HypixelMinigameGroup.SKYWARS, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Normal", "solo_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Insane", "solo_insane", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Ranked Mode", "ranked_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Normal", "teams_normal", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Insane", "teams_insane", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Mega Doubles", "mega_doubles", HypixelMinigameGroup.SKYWARS, true));
                gameBtn.add(new GuiButtonCustomize(this, "Hunters vs Beasts", "solo_insane_hunters_vs_beasts", HypixelMinigameGroup.SKYWARS, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SKYWARS_LAB.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Skywars Lobby", "sw", HypixelMinigameGroup.SKYWARS_LAB, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo TNT", "solo_insane_tnt_madness", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Rush", "solo_insane_rush", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Slime", "solo_insane_slime", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Lucky", "solo_insane_lucky", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team TNT", "teams_insane_tnt_madness", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Rush", "teams_insane_rush", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Slime", "teams_insane_slime", HypixelMinigameGroup.SKYWARS_LAB, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Lucky", "teams_insane_lucky", HypixelMinigameGroup.SKYWARS_LAB, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SMASH_HEROES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Smash Heroes Lobby", "sh", HypixelMinigameGroup.SMASH_HEROES, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo 1v1v1v1", "super_smash_solo_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams 2v2", "super_smash_2v2_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Teams 2v2v2", "super_smash_teams_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(this, "1v1 Mode", "super_smash_1v1_normal", HypixelMinigameGroup.SMASH_HEROES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Friends 1v1v1v1", "super_smash_friends_normal", HypixelMinigameGroup.SMASH_HEROES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SPEED_UHC.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Speed UHC Lobby", "suhc", HypixelMinigameGroup.SPEED_UHC, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "speed_solo_normal", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo Insane", "speed_solo_insane", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team", "speed_team_normal", HypixelMinigameGroup.SPEED_UHC, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team Insane", "speed_team_insane", HypixelMinigameGroup.SPEED_UHC, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.SURVIVAL_GAMES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Blitz Surival Game Lobby", "sg", HypixelMinigameGroup.SURVIVAL_GAMES, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "blitz_solo_normal", HypixelMinigameGroup.SURVIVAL_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Solo (No Kits)", "blitz_solo_nokits", HypixelMinigameGroup.SURVIVAL_GAMES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Team", "blitz_teams_normal", HypixelMinigameGroup.SURVIVAL_GAMES, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.TNT.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "TNT Lobby", "tnt", HypixelMinigameGroup.TNT, false));
                gameBtn.add(new GuiButtonCustomize(this, "TNT Run", "tnt_tntrun", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(this, "PvP Run", "tnt_pvprun", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(this, "Bow Spleef", "tnt_bowspleef", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(this, "TNT Tag", "tnt_tntag", HypixelMinigameGroup.TNT, true));
                gameBtn.add(new GuiButtonCustomize(this, "Wizards", "tnt_capture", HypixelMinigameGroup.TNT, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.UHC_CHAMPIONS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "UHC Champions Lobby", "uhc", HypixelMinigameGroup.UHC_CHAMPIONS, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.WARLORDS.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Warlords Lobby", "wl", HypixelMinigameGroup.WARLORDS, false));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, false));
                gameBtn.add(new GuiButtonCustomize(this, "Solo", "prototype_royale:solo", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Doubles", "prototype_royale:doubles", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
                gameBtn.add(new GuiButtonCustomize(this, "Squad", "prototype_royale:squad", HypixelMinigameGroup.PROTOTYPE_BATTLE_ROYALE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_HIDE_AND_SEEK.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_HIDE_AND_SEEK, false));
                gameBtn.add(new GuiButtonCustomize(this, "Prop Hunt", "prototype_hide_and_seek_party_pooper", HypixelMinigameGroup.PROTOTYPE_HIDE_AND_SEEK, true));
                gameBtn.add(new GuiButtonCustomize(this, "Party Pooper", "prototype_hide_and_seek_prop_hunt", HypixelMinigameGroup.PROTOTYPE_HIDE_AND_SEEK, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_KOTH.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_KOTH, false));
                gameBtn.add(new GuiButtonCustomize(this, "6v6", "prototype_koth_two_team", HypixelMinigameGroup.PROTOTYPE_KOTH, true));
                gameBtn.add(new GuiButtonCustomize(this, "3v3v3v3", "prototype_koth_four_team", HypixelMinigameGroup.PROTOTYPE_KOTH, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, false));
                gameBtn.add(new GuiButtonCustomize(this, "1v1", "prototype_bridge_1v1", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(this, "2v2", "prototype_bridge_2v2", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(this, "4v4", "prototype_bridge_4v4", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(this, "2v2v2v2", "prototype_bridge_2v2v2v2", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
                gameBtn.add(new GuiButtonCustomize(this, "3v3v3v3", "prototype_bridge_3v3v3v3", HypixelMinigameGroup.PROTOTYPE_THE_BRIDGE, true));
            }
            else if (ExtendedConfig.selectedHypixelMinigame == HypixelMinigameGroup.PROTOTYPE_ZOMBIES.ordinal())
            {
                gameBtn.add(new GuiButtonCustomize(this, "Prototype Lobby", "ptl", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, false));
                gameBtn.add(new GuiButtonCustomize(this, "Story Normal", "prototype_zombies_story_normal", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Story Hard", "prototype_zombies_story_hard", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Story RIP", "prototype_zombies_story_rip", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Endless Normal", "prototype_zombies_endless_normal", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Endless Hard", "prototype_zombies_endless_hard", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
                gameBtn.add(new GuiButtonCustomize(this, "Endless RIP", "prototype_zombies_endless_rip", HypixelMinigameGroup.PROTOTYPE_ZOMBIES, true));
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
                if (i >= 11)
                {
                    button.x = xPos2 - 241;
                    button.x += 21 * i;
                    button.y = 62;
                }
                this.buttonList.add(button);
            }
        }

        this.buttonList.forEach(button ->
        {
            if (!button.getClass().equals(GuiDropdownMinigames.class) && !(button.id >= 0 && button.id <= 102))
            {
                button.visible = false;
            }
        });
    }
}