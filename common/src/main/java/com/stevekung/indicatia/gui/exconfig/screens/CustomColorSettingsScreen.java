package com.stevekung.indicatia.gui.exconfig.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;

public class CustomColorSettingsScreen extends Screen
{
    private final Screen parent;

    CustomColorSettingsScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("menu.render_info_custom_color.title"), button -> this.minecraft.setScreen(new CustomRenderInfoColorSettingsScreen(this))));
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 175, 200, 20, CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.parent)));
    }

    @Override
    public void onClose()
    {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        GuiComponent.drawCenteredString(poseStack, this.font, LangUtils.translate("menu.custom_color.title"), this.width / 2, 15, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}