package com.stevekung.indicatia.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.realms.RealmsBridgeScreen;

public class ConfirmDisconnectScreen extends Screen
{
    private final Screen parent;

    public ConfirmDisconnectScreen(Screen parent)
    {
        super(NarratorChatListener.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, LangUtils.translateComponent("gui.yes"), button ->
        {
            if (this.minecraft.isConnectedToRealms())
            {
                this.minecraft.world.sendQuittingDisconnectingPacket();
                this.minecraft.loadWorld(null);
                RealmsBridgeScreen bridge = new RealmsBridgeScreen();
                bridge.func_231394_a_(new MainMenuScreen());
            }
            else
            {
                this.minecraft.world.sendQuittingDisconnectingPacket();
                this.minecraft.loadWorld(null);
                this.minecraft.displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
            }
        }));
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, LangUtils.translateComponent("gui.no"), button -> this.minecraft.displayGuiScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translateComponent("menu.confirm_disconnect"), this.width / 2, 70, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}