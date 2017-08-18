package stevekung.mods.indicatia.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.JsonUtil;

public class GuiNewChatUtil extends GuiChat
{
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;
    private static int page;
    private GuiButton swLobby;
    private GuiButton swSoloNormal;
    private GuiButton swSoloInsane;
    private GuiButton nextSWMode;
    private GuiButton swTeamNormal;
    private GuiButton swTeamInsane;
    private GuiButton swMegaMode;
    private GuiButton previousSWMode;

    public GuiNewChatUtil() {}

    public GuiNewChatUtil(String input)
    {
        super(input);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        boolean enableCPS = ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom");

        if (enableCPS)
        {
            this.buttonList.add(new GuiButton(0, this.width - 63, this.height - 35, 60, 20, "Reset CPS"));
            this.buttonList.add(new GuiCPSSlider(1, this.width - 165, this.height - 35, GuiCPSSlider.Options.CPS_OPACITY));
        }
        if (InfoUtil.INSTANCE.isHypixel())
        {
            String skywars = this.mc.world != null && this.mc.world.getScoreboard() != null && this.mc.world.getScoreboard().getObjectiveInDisplaySlot(1) != null ? this.mc.world.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().toLowerCase() : "";

            this.buttonList.add(new GuiButton(100, this.width - 63, enableCPS ? this.height - 56 : this.height - 35, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(101, this.width - 63, enableCPS ? this.height - 77 : this.height - 56, 60, 20, "Party Chat"));
            this.buttonList.add(new GuiButton(102, this.width - 63, enableCPS ? this.height - 98 : this.height - 77, 60, 20, "Guild Chat"));

            if (InfoUtil.INSTANCE.removeFormattingCodes(skywars).contains("skywars"))
            {
                this.buttonList.add(this.swLobby = new GuiButton(1000, this.width - 72, 2, 70, 20, "SW Lobby"));
                this.buttonList.add(this.swSoloNormal = new GuiButton(1001, this.width - 72, 23, 70, 20, "Solo Normal"));
                this.buttonList.add(this.swSoloInsane = new GuiButton(1002, this.width - 72, 44, 70, 20, "Solo Insane"));
                this.buttonList.add(this.nextSWMode = new GuiButton(150, this.width - 22, 65, 20, 20, ">"));
                this.buttonList.add(this.swTeamNormal = new GuiButton(1003, this.width - 72, 2, 70, 20, "Team Normal"));
                this.buttonList.add(this.swTeamInsane = new GuiButton(1004, this.width - 72, 23, 70, 20, "Team Insane"));
                this.buttonList.add(this.swMegaMode = new GuiButton(1005, this.width - 72, 44, 70, 20, "Mega Mode"));
                this.buttonList.add(this.previousSWMode = new GuiButton(151, this.width - 22, 65, 20, 20, "<"));
                this.swTeamNormal.visible = false;
                this.swTeamInsane.visible = false;
                this.swMegaMode.visible = false;
                this.previousSWMode.visible = false;
                this.swLobby.visible = false;
                this.swSoloNormal.visible = false;
                this.swSoloInsane.visible = false;
                this.nextSWMode.visible = false;
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (InfoUtil.INSTANCE.isHypixel())
        {
            String skywars = this.mc.world != null && this.mc.world.getScoreboard() != null && this.mc.world.getScoreboard().getObjectiveInDisplaySlot(1) != null ? this.mc.world.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().toLowerCase() : "";

            if (InfoUtil.INSTANCE.removeFormattingCodes(skywars).contains("skywars"))
            {
                if (GuiNewChatUtil.page == 0)
                {
                    this.swLobby.visible = true;
                    this.swSoloNormal.visible = true;
                    this.swSoloInsane.visible = true;
                    this.nextSWMode.visible = true;
                    this.swTeamNormal.visible = false;
                    this.swTeamInsane.visible = false;
                    this.swMegaMode.visible = false;
                    this.previousSWMode.visible = false;
                }
                else
                {
                    this.swLobby.visible = false;
                    this.swSoloNormal.visible = false;
                    this.swSoloInsane.visible = false;
                    this.nextSWMode.visible = false;
                    this.swTeamNormal.visible = true;
                    this.swTeamInsane.visible = true;
                    this.swMegaMode.visible = true;
                    this.previousSWMode.visible = true;
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String text = this.inputField.getText().trim();

            if (!text.isEmpty())
            {
                this.sendChatMessage(text);
            }
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        ExtendedConfig.save();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            String space = ConfigManager.enableRCPS ? " " : "";
            int minX = ExtendedConfig.CPS_X_OFFSET;
            int minY = ExtendedConfig.CPS_Y_OFFSET;
            int maxX = ExtendedConfig.CPS_X_OFFSET + this.fontRendererObj.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4;
            int maxY = ExtendedConfig.CPS_Y_OFFSET + 12;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY)
            {
                this.isDragging = true;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (ConfigManager.enableCPS && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (this.isDragging)
            {
                ExtendedConfig.CPS_X_OFFSET += mouseX - this.lastPosX;
                ExtendedConfig.CPS_Y_OFFSET += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
        case 0:
            ScaledResolution res = new ScaledResolution(this.mc);
            ExtendedConfig.CPS_X_OFFSET = res.getScaledWidth() / 2 - 36;
            ExtendedConfig.CPS_Y_OFFSET = res.getScaledHeight() / 2 - 5;
            ExtendedConfig.save();
            break;
        case 100:
            this.mc.player.sendChatMessage("/chat a");
            this.mc.player.sendMessage(new JsonUtil().text("Reset Hypixel Chat"));
            break;
        case 101:
            this.mc.player.sendChatMessage("/chat p");
            this.mc.player.sendMessage(new JsonUtil().text("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            this.mc.player.sendChatMessage("/chat g");
            this.mc.player.sendMessage(new JsonUtil().text("Set chat mode to Hypixel Guild Chat"));
            break;
        case 150:
            GuiNewChatUtil.page = 1;
            break;
        case 151:
            GuiNewChatUtil.page = 0;
            break;
        case 1000:
            this.mc.player.sendChatMessage("/lobby sw");
            break;
        case 1001:
            this.mc.player.sendChatMessage("/play solo_normal");
            break;
        case 1002:
            this.mc.player.sendChatMessage("/play solo_insane");
            break;
        case 1003:
            this.mc.player.sendChatMessage("/play teams_normal");
            break;
        case 1004:
            this.mc.player.sendChatMessage("/play teams_insane");
            break;
        case 1005:
            this.mc.player.sendChatMessage("/play mega_normal");
            break;
        }
    }
}