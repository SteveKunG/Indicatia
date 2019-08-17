package stevekung.mods.indicatia.gui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiDropdownMinigames.IDropboxCallback;
import stevekung.mods.indicatia.minigames.MinigameCommand;
import stevekung.mods.indicatia.minigames.MinigameData;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.client.gui.IEntityHoverChat;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class GuiIndicatiaChat implements IEntityHoverChat, IDropboxCallback
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
            if (this.prevSelect != ExtendedConfig.instance.selectedHypixelMinigame)
            {
                this.updateButton(buttonList, width, height);
                this.prevSelect = ExtendedConfig.instance.selectedHypixelMinigame;
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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("custom"))
        {
            String space = ExtendedConfig.instance.rcps ? " " : "";
            int minX = ExtendedConfig.instance.cpsCustomXOffset;
            int minY = ExtendedConfig.instance.cpsCustomYOffset;
            int maxX = ExtendedConfig.instance.cpsCustomXOffset + Minecraft.getMinecraft().fontRenderer.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
            int maxY = ExtendedConfig.instance.cpsCustomYOffset + 12;

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
        if (ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
                ExtendedConfig.instance.save();
            }
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("custom"))
        {
            if (this.isDragging)
            {
                ExtendedConfig.instance.cpsCustomXOffset += mouseX - this.lastPosX;
                ExtendedConfig.instance.cpsCustomYOffset += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (player == null)
        {
            return;
        }

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
            ExtendedConfig.instance.cpsCustomXOffset = res.getScaledWidth() / 2 - (ExtendedConfig.instance.rcps ? 36 : 16);
            ExtendedConfig.instance.cpsCustomYOffset = res.getScaledHeight() / 2 - 5;
            ExtendedConfig.instance.save();
            break;
        case 200:
            player.sendChatMessage("/chat a");
            player.sendMessage(JsonUtils.create("Reset Hypixel Chat"));
            break;
        case 201:
            player.sendChatMessage("/chat p");
            player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Party Chat"));
            break;
        case 202:
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
        ExtendedConfig.instance.save();
    }

    @Override
    public void handleMouseInput(int width, int height)
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
        }
    }

    @Override
    public void pageUp() {}

    @Override
    public void pageDown() {}

    @Override
    public void getSentHistory(int msgPos) {}

    @Override
    public void onSelectionChanged(GuiDropdownMinigames dropdown, int selection)
    {
        ExtendedConfig.instance.selectedHypixelMinigame = selection;
        ExtendedConfig.instance.save();
    }

    @Override
    public int getInitialSelection(GuiDropdownMinigames dropdown)
    {
        return ExtendedConfig.instance.selectedHypixelMinigame;
    }

    private void updateButton(List<GuiButton> buttonList, int width, int height)
    {
        Minecraft mc = Minecraft.getMinecraft();
        buttonList.clear();
        boolean enableCPS = ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("custom");

        if (enableCPS)
        {
            buttonList.add(new GuiButton(0, width - 63, height - 35, 60, 20, LangUtils.translate("message.reset_cps")));
        }
        if (InfoUtils.INSTANCE.isHypixel())
        {
            List<String> list = new LinkedList<>();

            for (MinigameData data : MinigameData.getMinigameData())
            {
                list.add(data.getName());
            }

            String max = Collections.max(list, Comparator.comparing(text -> text.length()));
            int length = mc.fontRenderer.getStringWidth(max) + 32;

            // hypixel chat
            buttonList.add(new GuiButton(200, width - 63, enableCPS ? height - 56 : height - 35, 60, 20, "Reset Chat"));
            buttonList.add(new GuiButton(201, width - 63, enableCPS ? height - 77 : height - 56, 60, 20, "Party Chat"));
            buttonList.add(new GuiButton(202, width - 63, enableCPS ? height - 98 : height - 77, 60, 20, "Guild Chat"));
            buttonList.add(this.dropdown = new GuiDropdownMinigames(this, width - length, 2, list));
            this.dropdown.width = length;
            this.prevSelect = ExtendedConfig.instance.selectedHypixelMinigame;

            List<GuiButtonCustomize> gameBtn = new LinkedList<>();
            int xPos2 = width - 99;

            if (this.prevSelect > list.size())
            {
                this.prevSelect = 0;
                ExtendedConfig.instance.selectedHypixelMinigame = 0;
            }

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
            if (!button.getClass().equals(GuiDropdownMinigames.class) && !(button.id >= 0 && button.id <= 202))
            {
                button.visible = false;
            }
        });
    }
}