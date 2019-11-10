package com.stevekung.indicatia.gui.screen;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.stevekung.indicatia.utils.MojangServerStatus;
import com.stevekung.indicatia.utils.MojangStatusChecker;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MojangStatusScreen extends Screen
{
    private List<String> statusList = new CopyOnWriteArrayList<>();
    private final Screen parent;
    private Button doneButton;
    private Button checkButton;
    private Button refreshButton;

    public MojangStatusScreen(Screen parent)
    {
        super(JsonUtils.create("Mojang Status Checker"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(this.doneButton = new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(this.refreshButton = new Button(this.width / 2 + 1, this.height / 6 + 145, 100, 20, LangUtils.translate("selectServer.refresh"), button ->
        {
            this.statusList.clear();
            this.checkButton.active = true;
            this.refreshButton.active = false;
        }));
        this.addButton(this.checkButton = new Button(this.width / 2 - 101, this.height / 6 + 145, 100, 20, LangUtils.translate("menu.check"), button ->
        {
            this.statusList.clear();
            Thread thread = new Thread(() ->
            {
                for (MojangStatusChecker checker : MojangStatusChecker.VALUES)
                {
                    MojangServerStatus status = checker.getStatus();
                    this.statusList.add(checker.getName() + ": " + status.getColor() + status.getStatus());
                }
                this.refreshButton.active = true;
                this.doneButton.active = true;
            });

            if (thread.getState() == Thread.State.NEW)
            {
                thread.start();
                this.checkButton.active = false;
                this.refreshButton.active = false;
                this.doneButton.active = false;
            }
        }));
        this.refreshButton.active = false;
    }

    @Override
    public void resize(Minecraft mc, int width, int height)
    {
        List<String> temp = this.statusList;
        this.statusList = temp;
        super.resize(mc, width, height);
    }

    @Override
    public void removed()
    {
        this.statusList.clear();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (this.doneButton.active && keyCode == 1)
        {
            this.statusList.clear();
            this.minecraft.displayGuiScreen(this.parent);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, "Mojang Status Checker", this.width / 2, 15, 16777215);
        int height = 0;

        for (String statusList : this.statusList)
        {
            this.drawString(this.font, statusList, this.width / 2 - 120, 35 + height, 16777215);
            height += 12;
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}