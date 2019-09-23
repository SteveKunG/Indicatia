package com.stevekung.indicatia.gui.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.stevekung.indicatia.config.CPSPosition;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.widget.DropdownMinigamesButton;
import com.stevekung.indicatia.gui.widget.DropdownMinigamesButton.IDropboxCallback;
import com.stevekung.indicatia.gui.widget.MinigameButton;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.minigames.MinigameCommand;
import com.stevekung.indicatia.minigames.MinigameData;
import com.stevekung.stevekungslib.client.gui.IChatScreen;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IndicatiaChatScreen implements IChatScreen, IDropboxCallback
{
    private boolean isDragging;
    private double lastPosX;
    private double lastPosY;
    private DropdownMinigamesButton dropdown;
    private int prevSelect = -1;

    @Override
    public void initGui(List<Widget> buttonList, int width, int height)
    {
        this.updateButton(buttonList, width, height);
    }

    @Override
    public void render(List<Widget> buttonList, int mouseX, int mouseY, float partialTicks)
    {
        for (Widget button : buttonList)
        {
            if (button instanceof MinigameButton)
            {
                MinigameButton customButton = (MinigameButton) button;
                customButton.drawRegion(mouseX, mouseY);
            }
        }
    }

    @Override
    public void tick(List<Widget> buttonList, int width, int height)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            if (this.prevSelect != ExtendedConfig.INSTANCE.selectedHypixelMinigame)
            {
                this.updateButton(buttonList, width, height);
                this.prevSelect = ExtendedConfig.INSTANCE.selectedHypixelMinigame;
            }

            boolean clicked = !this.dropdown.dropdownClicked;

            for (Widget button : buttonList)
            {
                if (button instanceof MinigameButton)
                {
                    MinigameButton buttonCustom = (MinigameButton) button;
                    buttonCustom.visible = clicked;
                }
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (ExtendedConfig.INSTANCE.cps && ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.CUSTOM)
        {
            String space = ExtendedConfig.INSTANCE.rcps ? " " : "";
            int minX = ExtendedConfig.INSTANCE.cpsCustomXOffset;
            int minY = ExtendedConfig.INSTANCE.cpsCustomYOffset;
            int maxX = ExtendedConfig.INSTANCE.cpsCustomXOffset + Minecraft.getInstance().fontRenderer.getStringWidth(InfoOverlays.getCPS() + space + InfoOverlays.getRCPS()) + 4;
            int maxY = ExtendedConfig.INSTANCE.cpsCustomYOffset + 12;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY)
            {
                this.isDragging = true;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        if (ExtendedConfig.INSTANCE.cps && ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.CUSTOM)
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
                ExtendedConfig.INSTANCE.save();
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int mouseEvent, double dragX, double dragY)
    {
        if (ExtendedConfig.INSTANCE.cps && ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.CUSTOM)
        {
            if (this.isDragging)
            {
                ExtendedConfig.INSTANCE.cpsCustomXOffset += mouseX - this.lastPosX;
                ExtendedConfig.INSTANCE.cpsCustomYOffset += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        ExtendedConfig.INSTANCE.save();
    }

    @Override
    public boolean mouseScrolled(double wheel)
    {
        if (this.dropdown != null && this.dropdown.dropdownClicked && this.dropdown.isHoverDropdown(Minecraft.getInstance().mouseHelper.getMouseX(), Minecraft.getInstance().mouseHelper.getMouseY()))
        {
            if (wheel > 1.0D)
            {
                wheel = 1.0D;
            }
            if (wheel < -1.0D)
            {
                wheel = -1.0D;
            }
            if (ClientUtils.isControlKeyDown())
            {
                wheel *= 7;
            }
            this.dropdown.scroll(wheel);
            return true;
        }
        return false;
    }

    @Override
    public void getSentHistory(int msgPos) {}

    @Override
    public void onSelectionChanged(DropdownMinigamesButton dropdown, int selection)
    {
        ExtendedConfig.INSTANCE.selectedHypixelMinigame = selection;
        ExtendedConfig.INSTANCE.save();
    }

    @Override
    public int getInitialSelection(DropdownMinigamesButton dropdown)
    {
        return ExtendedConfig.INSTANCE.selectedHypixelMinigame;
    }

    private void updateButton(List<Widget> buttonList, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        boolean enableCPS = ExtendedConfig.INSTANCE.cps && ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.CUSTOM;

        if (mc.player == null || !(mc.currentScreen instanceof ChatScreen))
        {
            return;
        }

        if (enableCPS)
        {
            buttonList.add(new Button(width - 63, height - 35, 60, 20, LangUtils.translate("menu.reset_cps"), button ->
            {
                ExtendedConfig.INSTANCE.cpsCustomXOffset = mc.mainWindow.getScaledWidth() / 2 - (ExtendedConfig.INSTANCE.rcps ? 36 : 16);
                ExtendedConfig.INSTANCE.cpsCustomYOffset = mc.mainWindow.getScaledHeight() / 2 - 5;
                ExtendedConfig.INSTANCE.save();
            }));
        }
        if (InfoUtils.INSTANCE.isHypixel())
        {
            List<String> list = new ArrayList<>();

            for (MinigameData data : MinigameData.getMinigames())
            {
                list.add(data.getName());
            }

            String max = Collections.max(list, Comparator.comparing(String::length));
            int length = mc.fontRenderer.getStringWidth(max) + 32;

            // hypixel chat
            buttonList.add(new Button(width - 63, enableCPS ? height - 56 : height - 35, 60, 20, "Reset Chat", button ->
            {
                player.sendChatMessage("/chat a");
                player.sendMessage(JsonUtils.create("Reset Hypixel Chat"));
            }));
            buttonList.add(new Button(width - 63, enableCPS ? height - 77 : height - 56, 60, 20, "Party Chat", button ->
            {
                player.sendChatMessage("/chat p");
                player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Party Chat"));
            }));
            buttonList.add(new Button(width - 63, enableCPS ? height - 98 : height - 77, 60, 20, "Guild Chat", button ->
            {
                player.sendChatMessage("/chat g");
                player.sendMessage(JsonUtils.create("Set chat mode to Hypixel Guild Chat"));
            }));
            buttonList.add(this.dropdown = new DropdownMinigamesButton(this, width - length, 2, list));
            this.dropdown.setWidth(length);
            this.prevSelect = ExtendedConfig.INSTANCE.selectedHypixelMinigame;

            List<MinigameButton> gameBtn = new ArrayList<>();
            int xPos2 = width - 99;

            if (this.prevSelect > list.size())
            {
                this.prevSelect = 0;
                ExtendedConfig.INSTANCE.hypixelMinigameScrollPos = 0;
                ExtendedConfig.INSTANCE.selectedHypixelMinigame = 0;
            }

            for (MinigameData data : MinigameData.getMinigames())
            {
                for (MinigameCommand command : data.getCommands())
                {
                    if (data.getName().equals(list.get(this.prevSelect)))
                    {
                        gameBtn.add(new MinigameButton(width, command.getName(), command.getCommand(), command.isMinigame()));
                    }
                }
            }

            for (int i = 0; i < gameBtn.size(); i++)
            {
                MinigameButton button = gameBtn.get(i);

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

        for (Widget button : buttonList)
        {
            if (button instanceof MinigameButton)
            {
                button.visible = false;
            }
        }
    }
}