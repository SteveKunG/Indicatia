package com.stevekung.indicatia.gui.screens;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.MojangServerStatus;
import com.stevekung.indicatia.utils.MojangStatusChecker;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class MojangStatusScreen extends Screen
{
    private final List<Component> statusList = Lists.newCopyOnWriteArrayList();
    private final Screen parent;
    private Button checkButton;
    private boolean check;

    public MojangStatusScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.parent)));
        this.addRenderableWidget(this.checkButton = new Button(this.width / 2 - 101, this.height / 6 + 145, 200, 20, LangUtils.translate("menu.check"), button ->
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.mojang_status.title"), this.width / 2, 15, 16777215);
        int height = 0;

        for (Component statusList : this.statusList)
        {
            GuiComponent.drawString(matrixStack, this.font, statusList, this.width / 2 - 120, 35 + height, 16777215);
            height += 12;
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}