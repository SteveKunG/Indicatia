package com.stevekung.indicatia.gui.screen;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.utils.MojangServerStatus;
import com.stevekung.indicatia.utils.MojangStatusChecker;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;

public class MojangStatusScreen extends Screen
{
    private List<String> statusList = new CopyOnWriteArrayList<>();
    private final Screen parent;
    private Button checkButton;
    private boolean check;

    public MojangStatusScreen(Screen parent)
    {
        super(NarratorChatListener.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, LangUtils.translateComponent("gui.done"), button ->
        {
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(this.checkButton = new Button(this.width / 2 - 101, this.height / 6 + 145, 200, 20, LangUtils.translateComponent("menu.check"), button ->
        {
            this.statusList.clear();
            Thread thread = new Thread(() ->
            {
                for (MojangStatusChecker checker : MojangStatusChecker.VALUES)
                {
                    MojangServerStatus status = checker.getStatus();
                    this.statusList.add(checker.getName() + ": " + status.getColor() + status.getStatus().getString());
                }
                this.check = false;
                this.checkButton.active = true;
            });

            if (thread.getState() == Thread.State.NEW)
            {
                this.check = true;
                thread.start();
                this.checkButton.active = false;
            }
        }));

        if (this.check)
        {
            this.checkButton.active = false;
        }
    }

    @Override
    public void resize(Minecraft mc, int width, int height)
    {
        List<String> temp = this.statusList;
        this.statusList = temp;
        super.resize(mc, width, height);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translateComponent("menu.mojang_status.title"), this.width / 2, 15, 16777215);
        int height = 0;

        for (String statusList : this.statusList)
        {
            AbstractGui.drawString(matrixStack, this.font, statusList, this.width / 2 - 120, 35 + height, 16777215);
            height += 12;
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}