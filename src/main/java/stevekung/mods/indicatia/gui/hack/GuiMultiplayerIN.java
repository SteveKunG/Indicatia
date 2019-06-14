package stevekung.mods.indicatia.gui.hack;

import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.client.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class GuiMultiplayerIN extends MultiplayerScreen
{
    public GuiMultiplayerIN(Screen parent)
    {
        super(parent);
    }

    @Override
    public void init()
    {
        this.buttons.clear();
        this.children.clear();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        if (this.initialized)
        {
            this.field_146803_h.updateSize(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.minecraft);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try
            {
                this.field_146800_B = new LanServerDetector.LanServerFindThread(this.lanServerList);
                this.field_146800_B.start();
            }
            catch (Exception e) {}

            this.field_146803_h = new ServerSelectionListIN(this, this.width, this.height, 32, this.height - 64, 36);
            this.field_146803_h.updateOnlineServers(this.savedServerList);
        }

        // Vanilla Stuff
        this.children.add(this.field_146803_h);
        this.field_146809_s = this.addButton(new Button(this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select"), button ->
        {
            this.connectToSelected();
        }));
        this.addButton(new Button(this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct"), button ->
        {
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
            this.minecraft.displayGuiScreen(new ServerListScreen(this::func_214290_d, this.selectedServer));
        }));
        this.addButton(new Button(this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add"), button ->
        {
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
            this.minecraft.displayGuiScreen(new AddServerScreen(this::func_214284_c, this.selectedServer));
        }));
        this.field_146810_r = this.addButton(new Button(this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit"), button ->
        {
            ServerSelectionList.Entry serverselectionlist$entry = this.field_146803_h.getSelected();

            if (serverselectionlist$entry instanceof ServerSelectionList.NormalEntry)
            {
                ServerData serverdata = ((ServerSelectionList.NormalEntry)serverselectionlist$entry).getServerData();
                this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
                this.selectedServer.copyFrom(serverdata);
                this.minecraft.displayGuiScreen(new AddServerScreen(this::func_214292_b, this.selectedServer));
            }

        }));
        this.field_146808_t = this.addButton(new Button(this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete"), button ->
        {
            ServerSelectionList.Entry serverselectionlist$entry = this.field_146803_h.getSelected();

            if (serverselectionlist$entry instanceof ServerSelectionList.NormalEntry)
            {
                String s = ((ServerSelectionList.NormalEntry)serverselectionlist$entry).getServerData().serverName;

                if (s != null)
                {
                    ITextComponent itextcomponent = new TranslationTextComponent("selectServer.deleteQuestion");
                    ITextComponent itextcomponent1 = new TranslationTextComponent("selectServer.deleteWarning", s);
                    String s1 = I18n.format("selectServer.deleteButton");
                    String s2 = I18n.format("gui.cancel");
                    this.minecraft.displayGuiScreen(new ConfirmScreen(this::func_214285_a, itextcomponent, itextcomponent1, s1, s2));
                }
            }

        }));
        this.addButton(new Button(this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh"), button ->
        {
            this.refreshServerList();
        }));
        this.addButton(new Button(this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel"), button ->
        {
            this.minecraft.displayGuiScreen(this.field_146798_g);
        }));
        this.func_214295_b();
    }

    @Override
    public void refreshServerList()
    {
        this.minecraft.displayGuiScreen(new GuiMultiplayerIN(this.field_146798_g));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        RenderUtils.disableLighting();
        String info1 = "Press <SHIFT> for";
        String info2 = "server version info";
        this.minecraft.fontRenderer.drawStringWithShadow(info1, 4, 3, ColorUtils.hexToRgb("#17F9DB"));
        this.minecraft.fontRenderer.drawStringWithShadow(info2, 4, 3 + this.minecraft.fontRenderer.FONT_HEIGHT + 1, ColorUtils.hexToRgb("#17F9DB"));
        RenderUtils.enableLighting();
    }
}