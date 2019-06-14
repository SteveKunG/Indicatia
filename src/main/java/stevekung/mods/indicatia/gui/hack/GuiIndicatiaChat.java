package stevekung.mods.indicatia.gui.hack;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import stevekung.mods.stevekungslib.client.gui.IEntityHoverChat;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

@OnlyIn(Dist.CLIENT)
public class GuiIndicatiaChat implements IEntityHoverChat, IDropboxCallback
{
    private boolean isDragging;
    private double lastPosX;
    private double lastPosY;
    private GuiDropdownMinigames dropdown;
    private int prevSelect = -1;

    @Override
    public void initGui(List<Widget> buttonList, int width, int height)
    {
        this.updateButton(buttonList, width, height);
    }

    @Override
    public void render(List<Widget> buttonList, int mouseX, int mouseY, float partialTicks)
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
    public void tick(List<Widget> buttonList, int width, int height)
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
    public void mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        if (ExtendedConfig.instance.cps && ExtendedConfig.instance.cpsPosition == CPSPosition.CUSTOM)
        {
            String space = ExtendedConfig.instance.rcps ? " " : "";
            int minX = ExtendedConfig.instance.cpsCustomXOffset;
            int minY = ExtendedConfig.instance.cpsCustomYOffset;
            int maxX = ExtendedConfig.instance.cpsCustomXOffset + Minecraft.getInstance().fontRenderer.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
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
    public void mouseReleased(double mouseX, double mouseY, int state)
    {
        if (ExtendedConfig.instance.cps && ExtendedConfig.instance.cpsPosition == CPSPosition.CUSTOM)
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
                ExtendedConfig.instance.save();
            }
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int mouseEvent, double dragX, double dragY)
    {
        if (ExtendedConfig.instance.cps && ExtendedConfig.instance.cpsPosition == CPSPosition.CUSTOM)
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
    public ITextComponent addEntityComponent(ITextComponent component)
    {
        for (String hide : HideNameData.getHideNameList())
        {
            if (component.getUnformattedComponentText().contains(hide))
            {
                component = JsonUtils.create(component.getUnformattedComponentText().replace(hide, TextFormatting.OBFUSCATED + hide + TextFormatting.RESET));
            }
        }
        return component;
    }

    @Override
    public void onGuiClosed()
    {
        ExtendedConfig.instance.save();
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
    public void keyTypedScrollDown() {}

    @Override
    public void keyTypedScrollUp() {}

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

    private void updateButton(List<Widget> buttonList, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        boolean enableCPS = ExtendedConfig.instance.cps && ExtendedConfig.instance.cpsPosition == CPSPosition.CUSTOM;

        if (enableCPS)
        {
            buttonList.add(new Button(width - 63, height - 35, 60, 20, LangUtils.translate("menu.reset_cps"), button ->
            {
                ExtendedConfig.instance.cpsCustomXOffset = mc.mainWindow.getScaledWidth() / 2 - (ExtendedConfig.instance.rcps ? 36 : 16);
                ExtendedConfig.instance.cpsCustomYOffset = mc.mainWindow.getScaledHeight() / 2 - 5;
                ExtendedConfig.instance.save();
            }));
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
            buttonList.add(this.dropdown = new GuiDropdownMinigames(this, width - length, 2, list));
            this.dropdown.setWidth(length);
            this.prevSelect = ExtendedConfig.instance.selectedHypixelMinigame;

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
            if (button instanceof GuiButtonCustomize)
            {
                button.visible = false;
            }
        });
    }
}