package stevekung.mods.indicatia.gui;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.utils.MojangServerStatus;
import stevekung.mods.indicatia.utils.MojangStatusChecker;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiMojangStatusChecker extends GuiScreen
{
    private final List<String> statusList = new CopyOnWriteArrayList<>();
    private final GuiScreen parent;
    private GuiButton doneButton;
    private GuiButton checkButton;
    private GuiButton refreshButton;

    public GuiMojangStatusChecker(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        this.statusList.clear();
        this.addButton(this.doneButton = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiMojangStatusChecker.this.statusList.clear();
                GuiMojangStatusChecker.this.mc.displayGuiScreen(GuiMojangStatusChecker.this.parent);
            }
        });
        this.addButton(this.refreshButton = new GuiButton(201, this.width / 2 + 1, this.height / 6 + 145, 100, 20, LangUtils.translate("selectServer.refresh"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiMojangStatusChecker.this.statusList.clear();
                GuiMojangStatusChecker.this.checkButton.enabled = true;
                GuiMojangStatusChecker.this.refreshButton.enabled = false;
            }
        });
        this.addButton(this.checkButton = new GuiButton(202, this.width / 2 - 101, this.height / 6 + 145, 100, 20, LangUtils.translate("menu.check"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiMojangStatusChecker.this.statusList.clear();
                Thread thread = new Thread(() ->
                {
                    try
                    {
                        Arrays.stream(MojangStatusChecker.values).forEach(checker ->
                        {
                            MojangServerStatus status = checker.getServiceStatus();
                            GuiMojangStatusChecker.this.statusList.add(checker.getName() + ": " + status.getColor() + status.getStatus());
                        });
                        GuiMojangStatusChecker.this.refreshButton.enabled = true;
                        GuiMojangStatusChecker.this.doneButton.enabled = true;
                    }
                    catch (Exception e) {}
                });

                if (thread.getState() == Thread.State.NEW)
                {
                    thread.start();
                    GuiMojangStatusChecker.this.checkButton.enabled = false;
                    GuiMojangStatusChecker.this.refreshButton.enabled = false;
                    GuiMojangStatusChecker.this.doneButton.enabled = false;
                }
            }
        });
        this.refreshButton.enabled = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (this.doneButton.enabled && keyCode == 1)
        {
            this.statusList.clear();
            this.mc.displayGuiScreen(null);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Mojang Status Checker", this.width / 2, 15, 16777215);
        int height = 0;

        for (String statusList : this.statusList)
        {
            this.drawString(this.fontRenderer, statusList, this.width / 2 - 120, 35 + height, 16777215);
            height += 12;
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}