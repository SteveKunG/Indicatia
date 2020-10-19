package com.stevekung.indicatia.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.text.StringTextComponent;

public class ConfirmDisconnectScreen extends Screen
{
    private final Screen parent;

    public ConfirmDisconnectScreen(Screen parent)
    {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, DialogTexts.GUI_YES, button ->
        {
            if (this.minecraft.isConnectedToRealms())
            {
                this.minecraft.world.sendQuittingDisconnectingPacket();
                this.minecraft.unloadWorld();
                RealmsBridgeScreen bridge = new RealmsBridgeScreen();
                bridge.func_231394_a_(new MainMenuScreen());
            }
            else
            {
                this.minecraft.world.sendQuittingDisconnectingPacket();
                this.minecraft.unloadWorld();
                this.minecraft.displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
            }
        }));
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, DialogTexts.GUI_NO, button -> this.minecraft.displayGuiScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.confirm_disconnect"), this.width / 2, 70, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}