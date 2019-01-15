package stevekung.mods.indicatia.gui.hack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiButtonCustomize;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames.IDropboxCallback;
import stevekung.mods.indicatia.minigames.MinigameCommand;
import stevekung.mods.indicatia.minigames.MinigameData;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.client.gui.IEntityHoverChat;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GuiIndicatiaChat implements IEntityHoverChat, IDropboxCallback
{
    private boolean isDragging;
    private double lastPosX;
    private double lastPosY;
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
                    buttonCustom.visible = clicked;
                }
            });
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom"))
        {
            String space = ExtendedConfig.rcps ? " " : "";
            int minX = ExtendedConfig.cpsCustomXOffset;
            int minY = ExtendedConfig.cpsCustomYOffset;
            int maxX = ExtendedConfig.cpsCustomXOffset + Minecraft.getInstance().fontRenderer.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
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
    public void mouseReleased(double mouseX, double mouseY, int state)
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
    public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, long timeSinceLastClick)
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
    public void actionPerformed(GuiButton button)//TODO
    {

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
//        Minecraft mc = Minecraft.getInstance();TODO
//        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
//        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
//
//        if (this.dropdown != null && this.dropdown.dropdownClicked && this.dropdown.isHoverDropdown(mouseX, mouseY))
//        {
//            int i = Mouse.getEventDWheel();
//
//            if (i != 0)
//            {
//                if (i > 1)
//                {
//                    i = -1;
//                }
//                if (i < -1)
//                {
//                    i = 1;
//                }
//                if (GuiScreen.isCtrlKeyDown())
//                {
//                    i *= 7;
//                }
//                this.dropdown.scroll(i);
//            }
//            if (Mouse.getEventButtonState())
//            {
//                // hacked mouse clicked :D
//                int event = Mouse.getEventButton();
//                this.mouseClicked(mouseX, mouseY, event);
//            }
//        }
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
        Minecraft mc = Minecraft.getInstance();
        EntityPlayerSP player = mc.player;
        boolean enableCPS = ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom");

        if (enableCPS)
        {
            buttonList.add(new GuiButton(0, width - 63, height - 35, 60, 20, LangUtils.translate("menu.reset_cps"))
            {
                @Override
                public void onClick(double mouseX, double mouseZ)
                {
                    ExtendedConfig.cpsCustomXOffset = mc.mainWindow.getScaledWidth() / 2 - (ExtendedConfig.rcps ? 36 : 16);
                    ExtendedConfig.cpsCustomYOffset = mc.mainWindow.getScaledHeight() / 2 - 5;
                    ExtendedConfig.save();
                }
            });
        }
        if (InfoUtils.INSTANCE.isHypixel())
        {
            List<String> list = new LinkedList<>();

            for (MinigameData data : MinigameData.getMinigameData())
            {
                list.add(data.getName());
            }

            String max = Collections.max(list, Comparator.comparing(String::length));
            int length = mc.fontRenderer.getStringWidth(max) + 32;

            // hypixel chat
            buttonList.add(new GuiButton(100, width - 63, enableCPS ? height - 56 : height - 35, 60, 20, "Reset Chat")
            {
                @Override
                public void onClick(double mouseX, double mouseZ)
                {
                    player.sendChatMessage("/chat a");
                    player.sendMessage(JsonUtils.create("Reset Hypixel Chat"));
                }
            });
            buttonList.add(new GuiButton(101, width - 63, enableCPS ? height - 77 : height - 56, 60, 20, "Party Chat")
            {
                @Override
                public void onClick(double mouseX, double mouseZ)
                {
                    player.sendChatMessage("/chat p");
                    player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Party Chat"));
                }
            });
            buttonList.add(new GuiButton(102, width - 63, enableCPS ? height - 98 : height - 77, 60, 20, "Guild Chat")
            {
                @Override
                public void onClick(double mouseX, double mouseZ)
                {
                    player.sendChatMessage("/chat g");
                    player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Guild Chat"));
                }
            });
            buttonList.add(this.dropdown = new GuiDropdownMinigames(this, width - length, 2, list));
            this.dropdown.width = length;
            this.prevSelect = ExtendedConfig.selectedHypixelMinigame;

            List<GuiButtonCustomize> gameBtn = new LinkedList<>();
            int xPos2 = width - 99;

            for (MinigameData data : MinigameData.getMinigameData())
            {
                for (MinigameCommand command : data.getCommands())
                {
                    if (data.getName().equals(list.get(this.prevSelect)))
                    {
                        gameBtn.add(new GuiButtonCustomize(width, command.getName(), command.getCommand(), command.isMinigame()));
                    }
                }
            }

            for (int i = 0; i < gameBtn.size(); i++)
            {
                GuiButtonCustomize button = gameBtn.get(i);

                if (i >= 6 && i <= 10)
                {
                    button.x = xPos2 - 136;
                    button.y = 41;
                }
                else if (i >= 11 && i <= 15)
                {
                    button.x = xPos2 - 241;
                    button.y = 62;
                }
                else if (i >= 16 && i <= 20)
                {
                    button.x = xPos2 - 346;
                    button.y = 83;
                }
                else if (i >= 21)
                {
                    button.x = xPos2 - 451;
                    button.y = 104;
                }
                button.x += 21 * i;
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