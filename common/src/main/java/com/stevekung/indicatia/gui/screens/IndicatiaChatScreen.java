package com.stevekung.indicatia.gui.screens;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.gui.components.DropdownMinigamesButton;
import com.stevekung.indicatia.gui.components.DropdownMinigamesButton.IDropboxCallback;
import com.stevekung.indicatia.gui.components.MinigameButton;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.MinigameData;
import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.stevekungslib.client.event.ScreenEvents;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.ItemUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import dev.architectury.event.EventResult;
import me.shedaniel.architectury.event.EventResult;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;

public class IndicatiaChatScreen implements IDropboxCallback
{
    private DropdownMinigamesButton dropdown;
    private int prevSelect = -1;
    private ChatMode mode = ChatMode.ALL;

    public IndicatiaChatScreen()
    {
        ScreenEvents.CHAT_SCREEN_INIT.register(this::onChatInit);
        ScreenEvents.CHAT_SCREEN_RENDER_PRE.register(this::onChatRenderPre);
        ScreenEvents.CHAT_SCREEN_RENDER_POST.register(this::onChatRenderPost);
        ScreenEvents.CHAT_SCREEN_TICK.register(this::onChatTick);
        ScreenEvents.CHAT_SCREEN_CLOSE.register(this::onChatClose);
        ScreenEvents.CHAT_SCREEN_MOUSE_SCROLL.register(this::onChatMouseScrolled);
    }

    private void onChatInit(List<Widget> buttons, List<GuiEventListener> children, int width, int height)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            this.updateButton(buttons, children, width, height);

            if (IndicatiaSettings.INSTANCE.chatMode < ChatMode.values().length)
            {
                this.mode = ChatMode.values()[IndicatiaSettings.INSTANCE.chatMode];
            }
        }
    }

    private void onChatRenderPre(List<Widget> buttons, List<GuiEventListener> children, PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        if (InfoUtils.INSTANCE.isHypixel() && PlatformConfig.getHypixelChatMode())
        {
            Minecraft mc = Minecraft.getInstance();
            MutableComponent chatMode = LangUtils.translate("menu.chat_mode").copy().append(": ").append(LangUtils.translate(this.mode.desc).copy().withStyle(this.mode.color, ChatFormatting.BOLD));
            int x = 4;
            int y = mc.screen.height - 30;
            GuiComponent.fill(poseStack, x - 2, y - 3, x + mc.font.width(chatMode) + 2, y + 10, ColorUtils.to32Bit(0, 0, 0, 128));
            mc.font.drawShadow(poseStack, chatMode, x, y, ColorUtils.toDecimal(255, 255, 255));
        }
    }

    private void onChatRenderPost(List<Widget> buttons, List<GuiEventListener> children, PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        if (InfoUtils.INSTANCE.isHypixel() && PlatformConfig.getHypixelDropdownShortcut())
        {
            for (Widget button : buttons)
            {
                if (button instanceof MinigameButton customButton)
                {
                    customButton.render(poseStack, mouseX, mouseY);
                }
            }
        }
    }

    private void onChatTick(List<Widget> buttons, List<GuiEventListener> children, int width, int height)
    {
        if (InfoUtils.INSTANCE.isHypixel() && PlatformConfig.getHypixelDropdownShortcut())
        {
            if (this.prevSelect != IndicatiaSettings.INSTANCE.selectedHypixelMinigame)
            {
                this.updateButton(buttons, children, width, height);
                this.prevSelect = IndicatiaSettings.INSTANCE.selectedHypixelMinigame;
            }

            boolean clicked = !this.dropdown.dropdownClicked;

            for (Widget button : buttons)
            {
                if (button instanceof MinigameButton buttonCustom)
                {
                    buttonCustom.visible = clicked;
                }
            }
        }
    }

    private void onChatClose(List<Widget> buttons, List<GuiEventListener> children)
    {
        IndicatiaSettings.INSTANCE.save();
    }

    private EventResult onChatMouseScrolled(List<Widget> buttons, List<GuiEventListener> children, double mouseX, double mouseY, double scrollDelta)
    {
        double delta = scrollDelta;

        if (PlatformConfig.getHypixelDropdownShortcut() && this.dropdown != null && this.dropdown.dropdownClicked && this.dropdown.isHoverDropdown(mouseX, mouseY))
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
            return EventResult.interruptTrue();
        }
        return EventResult.pass();
    }

    @Override
    public void onSelectionChanged(DropdownMinigamesButton dropdown, int selection)
    {
        IndicatiaSettings.INSTANCE.selectedHypixelMinigame = selection;
        IndicatiaSettings.INSTANCE.save();
    }

    @Override
    public int getInitialSelection(DropdownMinigamesButton dropdown)
    {
        return IndicatiaSettings.INSTANCE.selectedHypixelMinigame;
    }

    private void updateButton(List<AbstractWidget> buttons, List<GuiEventListener> children, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        buttons.clear();
        children.clear();

        if (player == null || !(mc.screen instanceof ChatScreen))
        {
            return;
        }

        List<String> list = Lists.newArrayList();

        for (MinigameData data : MinigameData.DATA)
        {
            list.add(data.getName());
        }

        String max = Collections.max(list, Comparator.comparing(String::length));
        int length = mc.font.width(max) + 32;

        if (PlatformConfig.getHypixelChatMode())
        {
            for (ChatMode mode : ChatMode.values())
            {
                buttons.add(new Button(width - mode.x, height - mode.y, mode.width, mode.height, mode.message, button ->
                {
                    this.mode = mode;
                    player.chat(mode.command);
                    IndicatiaSettings.INSTANCE.chatMode = mode.ordinal();
                }));
            }
        }

        if (PlatformConfig.getHypixelDropdownShortcut())
        {
            buttons.add(this.dropdown = new DropdownMinigamesButton(this, width - length, 2, list));
            this.dropdown.setWidth(length);
            this.prevSelect = IndicatiaSettings.INSTANCE.selectedHypixelMinigame;

            List<MinigameButton> gameBtn = Lists.newArrayList();
            int xPos2 = width - 99;

            if (this.prevSelect > list.size())
            {
                this.prevSelect = 0;
                IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos = 0;
                IndicatiaSettings.INSTANCE.selectedHypixelMinigame = 0;
            }

            for (MinigameData data : MinigameData.DATA)
            {
                for (MinigameData.Command command : data.getCommands())
                {
                    if (data.getName().equals(list.get(this.prevSelect)))
                    {
                        ItemStack skull = ItemStack.EMPTY;

                        if (!StringUtil.isNullOrEmpty(command.getUUID()))
                        {
                            skull = ItemUtils.getSkullItemStack(command.getUUID(), command.getTexture());
                        }
                        gameBtn.add(new MinigameButton(width, TextComponentUtils.component(command.getName()), command.isMinigame(), button -> player.chat(command.getCommand().startsWith("/") ? command.getCommand() : command.isMinigame() ? "/play " + command.getCommand() : "/lobby " + command.getCommand()), skull));
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
            if (button instanceof MinigameButton minigameButton)
            {
                minigameButton.visible = false;
            }
        }
        children.addAll(buttons);
    }

    public enum ChatMode
    {
        ALL("menu.chat_mode.all_chat", ChatFormatting.GRAY, 23, 35, 20, 20, "A", "/chat a"),
        PARTY("menu.chat_mode.party_chat", ChatFormatting.BLUE, 23, 56, 20, 20, "P", "/chat p"),
        GUILD("menu.chat_mode.guild_chat", ChatFormatting.DARK_GREEN, 23, 77, 20, 20, "G", "/chat g");

        protected final String desc;
        protected final ChatFormatting color;
        protected final int x;
        protected final int y;
        protected final int width;
        protected final int height;
        protected final Component message;
        public final String command;

        ChatMode(String desc, ChatFormatting color, int x, int y, int width, int height, String message, String command)
        {
            this.desc = desc;
            this.color = color;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.message = LangUtils.translate(message);
            this.command = command;
        }
    }
}