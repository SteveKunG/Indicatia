package com.stevekung.indicatia.gui.screen;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.utils.MojangServerStatus;
import com.stevekung.indicatia.utils.MojangStatusChecker;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class MojangStatusScreen extends Screen
{
    private List<ITextComponent> statusList = new CopyOnWriteArrayList<>();
    private final Screen parent;
    private Button checkButton;
    private boolean check;

    public MojangStatusScreen(Screen parent)
    {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, DialogTexts.GUI_DONE, button ->
        {
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(this.checkButton = new Button(this.width / 2 - 101, this.height / 6 + 145, 200, 20, LangUtils.translate("menu.check"), button ->
        {
            this.statusList.clear();
            Thread thread = new Thread(() ->
            {
                for (MojangStatusChecker checker : MojangStatusChecker.VALUES)
                {
                    MojangServerStatus status = checker.getStatus();
                    this.statusList.add(TextComponentUtils.component(checker.getName() + ": ").append(status.getStatus()));
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
        List<ITextComponent> temp = this.statusList;
        this.statusList = temp;
        super.resize(mc, width, height);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.mojang_status.title"), this.width / 2, 15, 16777215);
        int height = 0;

        for (ITextComponent statusList : this.statusList)
        {
            AbstractGui.drawString(matrixStack, this.font, statusList, this.width / 2 - 120, 35 + height, 16777215);
            height += 12;
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}