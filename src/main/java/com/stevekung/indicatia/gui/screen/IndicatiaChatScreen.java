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
import com.stevekung.indicatia.utils.MinigameData;
import com.stevekung.stevekungslib.client.event.ChatScreenEvent;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IExtensibleEnum;
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

        if (ExtendedConfig.INSTANCE.chatMode < ChatMode.values().length)
        {
            this.mode = ChatMode.values()[ExtendedConfig.INSTANCE.chatMode];
        }
    }

    @SubscribeEvent
    public void onChatRenderPre(ChatScreenEvent.RenderPre event)
    {
        if (IndicatiaConfig.GENERAL.enableHypixelChatMode.get() && InfoUtils.INSTANCE.isHypixel())
        {
            Minecraft mc = Minecraft.getInstance();
            IFormattableTextComponent chatMode = LangUtils.translate("menu.chat_mode").deepCopy().appendString(": ").append(LangUtils.translate(this.mode.desc).deepCopy().mergeStyle(this.mode.color, TextFormatting.BOLD));
            int x = 4;
            int y = mc.currentScreen.height - 30;
            AbstractGui.fill(event.getMatrixStack(), x - 2, y - 3, x + mc.fontRenderer.getStringPropertyWidth(chatMode) + 2, y + 10, ColorUtils.to32Bit(0, 0, 0, 128));
            mc.fontRenderer.func_243246_a(event.getMatrixStack(), chatMode, x, y, ColorUtils.toDecimal(255, 255, 255));
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
                    customButton.render(event.getMatrixStack(), event.getMouseX(), event.getMouseY());
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
                for (ChatMode mode : ChatMode.values())
                {
                    buttons.add(new Button(width - mode.x, height - mode.y, mode.width, mode.height, mode.message, button ->
                    {
                        this.mode = mode;
                        player.sendChatMessage(mode.command);
                        ExtendedConfig.INSTANCE.chatMode = mode.ordinal();
                    }));
                }
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
                    for (MinigameData.Command command : data.getCommands())
                    {
                        if (data.getName().equals(list.get(this.prevSelect)))
                        {
                            ItemStack skull = ItemStack.EMPTY;

                            if (!StringUtils.isNullOrEmpty(command.getUUID()))
                            {
                                skull = this.getSkullItemStack(command.getUUID(), command.getTexture());
                            }
                            gameBtn.add(new MinigameButton(width, TextComponentUtils.component(command.getName()), command.isMinigame(), button -> player.sendChatMessage(command.getCommand().startsWith("/") ? command.getCommand() : command.isMinigame() ? "/play " + command.getCommand() : "/lobby " + command.getCommand()), skull));
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

    private ItemStack getSkullItemStack(String skullId, String skullValue)
    {
        ItemStack itemStack = new ItemStack(Items.PLAYER_HEAD);
        CompoundNBT compound = new CompoundNBT();
        CompoundNBT properties = new CompoundNBT();
        properties.putString("Id", skullId);
        CompoundNBT texture = new CompoundNBT();
        ListNBT list = new ListNBT();
        CompoundNBT value = new CompoundNBT();
        value.putString("Value", skullValue);
        list.add(value);
        texture.put("textures", list);
        properties.put("Properties", texture);
        compound.put("SkullOwner", properties);
        itemStack.setTag(compound);
        return itemStack;
    }

    public enum ChatMode implements IExtensibleEnum
    {
        ALL("menu.chat_mode.all_chat", TextFormatting.GRAY, 23, 35, 20, 20, "A", "/chat a"),
        PARTY("menu.chat_mode.party_chat", TextFormatting.BLUE, 23, 56, 20, 20, "P", "/chat p"),
        GUILD("menu.chat_mode.guild_chat", TextFormatting.DARK_GREEN, 23, 77, 20, 20, "G", "/chat g");

        protected final String desc;
        protected final TextFormatting color;
        protected final int x;
        protected final int y;
        protected final int width;
        protected final int height;
        protected final ITextComponent message;
        protected final String command;

        private ChatMode(String desc, TextFormatting color, int x, int y, int width, int height, String message, String command)
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

        public static ChatMode create(String name, String desc, TextFormatting color, int x, int y, int width, int height, String message, String command)
        {
            throw new IllegalStateException("Enum not extended");
        }
    }
}