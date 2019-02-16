package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.*;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.gui.hack.GuiMultiplayerIN;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiConfirmDisconnect extends GuiScreen
{
    @Override
    public void initGui()
    {
        this.addButton(new GuiButton(0, this.width / 2 - 155, this.height / 6 + 96, 150, 20, LangUtils.translate("gui.yes"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                if (GuiConfirmDisconnect.this.mc.isConnectedToRealms())
                {
                    GuiConfirmDisconnect.this.mc.world.sendQuittingDisconnectingPacket();
                    GuiConfirmDisconnect.this.mc.loadWorld(null);
                    RealmsBridge bridge = new RealmsBridge();
                    bridge.switchToRealms(new GuiMainMenu());
                }
                else
                {
                    GuiConfirmDisconnect.this.mc.world.sendQuittingDisconnectingPacket();
                    GuiConfirmDisconnect.this.mc.loadWorld(null);

                    if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get())
                    {
                        GuiConfirmDisconnect.this.mc.displayGuiScreen(new GuiMultiplayerIN(new GuiMainMenu()));
                    }
                    else
                    {
                        GuiConfirmDisconnect.this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                    }
                }
            }
        });
        this.addButton(new GuiButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, LangUtils.translate("gui.no"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiConfirmDisconnect.this.mc.displayGuiScreen(new GuiIngameMenu());
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("menu.confirm_disconnect"), this.width / 2, 70, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}