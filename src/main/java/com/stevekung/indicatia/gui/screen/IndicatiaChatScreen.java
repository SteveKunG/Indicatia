package com.stevekung.indicatia.gui.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.gui.widget.DropdownMinigamesButton;
import com.stevekung.indicatia.gui.widget.DropdownMinigamesButton.IDropboxCallback;
import com.stevekung.indicatia.gui.widget.MinigameButton;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.minigames.MinigameCommand;
import com.stevekung.indicatia.minigames.MinigameData;
import com.stevekung.stevekungslib.client.event.ChatScreenEvent;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IndicatiaChatScreen implements IDropboxCallback
{
    private DropdownMinigamesButton dropdown;
    private int prevSelect = -1;
    private ChatMode mode = ChatMode.ALL;

    @SubscribeEvent
    public void onChatInit(ChatScreenEvent.Init event)
    {
        this.updateButton(event.getButtons(), event.getChildren(), event.getWidth(), event.getHeight());
        this.mode = ChatMode.VALUES[ExtendedConfig.INSTANCE.chatMode];
    }

    @SubscribeEvent
    public void onChatRenderPre(ChatScreenEvent.RenderPre event)
    {
        if (IndicatiaConfig.GENERAL.enableHypixelChatMode.get() && InfoUtils.INSTANCE.isHypixel())
        {
            Minecraft mc = Minecraft.getInstance();
            String chatMode = LangUtils.translate("menu.chat_mode") + ": " + JsonUtils.create(this.mode.getDesc()).setStyle(new Style().setColor(this.mode.getColor()).setBold(true)).getFormattedText();
            int x = 4;
            int y = mc.currentScreen.height - 30;
            AbstractGui.fill(x - 2, y - 3, x + mc.fontRenderer.getStringWidth(chatMode) + 2, y + 10, ColorUtils.to32BitColor(128, 0, 0, 0));
            mc.fontRenderer.drawStringWithShadow(chatMode, x, y, ColorUtils.rgbToDecimal(255, 255, 255));
        }
    }

    @SubscribeEvent
    public void onChatRenderPost(ChatScreenEvent.RenderPost event)
    {
        if (IndicatiaConfig.GENERAL.enableHypixelDropdownShortcutGame.get())
        {
            for (Widget button : event.getButtons())
            {
                if (button instanceof MinigameButton)
                {
                    MinigameButton customButton = (MinigameButton) button;
                    customButton.render(event.getMouseX(), event.getMouseY());
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatTick(ChatScreenEvent.Tick event)
    {
        if (InfoUtils.INSTANCE.isHypixel() && IndicatiaConfig.GENERAL.enableHypixelDropdownShortcutGame.get())
        {
            if (this.prevSelect != ExtendedConfig.INSTANCE.selectedHypixelMinigame)
            {
                this.updateButton(event.getButtons(), event.getChildren(), event.getWidth(), event.getHeight());
                this.prevSelect = ExtendedConfig.INSTANCE.selectedHypixelMinigame;
            }

            boolean clicked = !this.dropdown.dropdownClicked;

            for (Widget button : event.getButtons())
            {
                if (button instanceof MinigameButton)
                {
                    MinigameButton buttonCustom = (MinigameButton) button;
                    buttonCustom.visible = clicked;
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatClose(ChatScreenEvent.Close event)
    {
        ExtendedConfig.INSTANCE.save();
    }

    @SubscribeEvent
    public void onChatMouseScrolled(ChatScreenEvent.MouseScroll event)
    {
        double delta = event.getScrollDelta();

        if (IndicatiaConfig.GENERAL.enableHypixelDropdownShortcutGame.get() && this.dropdown != null && this.dropdown.dropdownClicked && this.dropdown.isHoverDropdown(event.getMouseX(), event.getMouseY()))
        {
            if (delta > 1.0D)
            {
                delta = 1.0D;
            }
            if (delta < -1.0D)
            {
                delta = -1.0D;
            }
            if (ClientUtils.isControlKeyDown())
            {
                delta *= 7;
            }
            this.dropdown.scroll(delta);
            event.setCanceled(true);
        }
    }

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

    private void updateButton(List<Widget> buttons, List<IGuiEventListener> children, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        buttons.clear();
        children.clear();

        if (player == null || !(mc.currentScreen instanceof ChatScreen))
        {
            return;
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

            if (IndicatiaConfig.GENERAL.enableHypixelChatMode.get())
            {
                buttons.add(new Button(width - 23, height - 35, 20, 20, "A", button ->
                {
                    this.mode = ChatMode.ALL;
                    player.sendChatMessage("/chat a");
                    ExtendedConfig.INSTANCE.chatMode = 0;
                }));
                buttons.add(new Button(width - 23, height - 56, 20, 20, "P", button ->
                {
                    this.mode = ChatMode.PARTY;
                    player.sendChatMessage("/chat p");
                    ExtendedConfig.INSTANCE.chatMode = 1;
                }));
                buttons.add(new Button(width - 23, height - 77, 20, 20, "G", button ->
                {
                    this.mode = ChatMode.GUILD;
                    player.sendChatMessage("/chat g");
                    ExtendedConfig.INSTANCE.chatMode = 2;
                }));
            }

            if (IndicatiaConfig.GENERAL.enableHypixelDropdownShortcutGame.get())
            {
                buttons.add(this.dropdown = new DropdownMinigamesButton(this, width - length, 2, list));
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
                            gameBtn.add(new MinigameButton(width, command.getName(), command.isMinigame(), button -> player.sendChatMessage(command.getCommand())));
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
                    buttons.add(button);
                }
            }

            for (Widget button : buttons)
            {
                if (button instanceof MinigameButton)
                {
                    button.visible = false;
                }
            }
            children.addAll(buttons);
        }
    }

    enum ChatMode
    {
        ALL("/achat", "menu.chat_mode.all_chat", TextFormatting.GRAY),
        PARTY("/pchat", "menu.chat_mode.party_chat", TextFormatting.BLUE),
        GUILD("/gchat", "menu.chat_mode.guild_chat", TextFormatting.DARK_GREEN);

        private final String command;
        private final String desc;
        private final TextFormatting color;
        protected static final ChatMode[] VALUES = ChatMode.values();

        private ChatMode(String command, String desc, TextFormatting color)
        {
            this.command = command;
            this.desc = desc;
            this.color = color;
        }

        public String getCommand()
        {
            return this.command;
        }

        public String getDesc()
        {
            return LangUtils.translate(this.desc);
        }

        public TextFormatting getColor()
        {
            return this.color;
        }
    }
}