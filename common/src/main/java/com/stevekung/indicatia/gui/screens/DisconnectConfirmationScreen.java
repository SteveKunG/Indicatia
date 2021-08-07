package com.stevekung.indicatia.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.RealmsBridge;

public class DisconnectConfirmationScreen extends Screen
{
    private final Screen parent;

    public DisconnectConfirmationScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, CommonComponents.GUI_YES, button ->
        {
            boolean flag = this.minecraft.isLocalServer();
            boolean flag1 = this.minecraft.isConnectedToRealms();
            this.minecraft.level.disconnect();

            if (flag)
            {
                this.minecraft.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
            }
            else
            {
                this.minecraft.clearLevel();
            }

            if (flag)
            {
                this.minecraft.setScreen(new TitleScreen());
            }
            else if (flag1)
            {
                RealmsBridge realmsbridgescreen = new RealmsBridge();
                realmsbridgescreen.switchToRealms(new TitleScreen());
            }
            else
            {
                this.minecraft.setScreen(new JoinMultiplayerScreen(new TitleScreen()));
            }
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, CommonComponents.GUI_NO, button -> this.minecraft.setScreen(this.parent)));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.confirm_disconnect"), this.width / 2, 70, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}