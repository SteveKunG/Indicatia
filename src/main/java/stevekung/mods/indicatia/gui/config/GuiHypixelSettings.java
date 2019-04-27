//package stevekung.mods.indicatia.gui.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.gui.IGuiEventListener;
//import net.minecraft.client.gui.Screen;
//import net.minecraft.client.gui.widget.ButtonWidget;
//import stevekung.mods.indicatia.config.ExtendedConfig;
//import stevekung.mods.stevekungslib.utils.LangUtils;
//
//@Environment(EnvType.CLIENT)
//public class GuiHypixelSettings extends Screen
//{
//    private final Screen parent;
//    private GuiConfigButtonRowList optionsRowList;
//    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
//
//    static
//    {
//        OPTIONS.add(ExtendedConfig.Options.RIGHT_CLICK_ADD_PARTY);
//    }
//
//    GuiHypixelSettings(Screen parent)
//    {
//        this.parent = parent;
//    }
//
//    @Override
//    public void init()
//    {
//        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, LangUtils.translate("gui.done"), (buttonWidget) ->
//        {
//            ExtendedConfig.save();
//            GuiHypixelSettings.this.minecraft.openScreen(GuiHypixelSettings.this.parent);
//        }));
//
//        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
//        options = OPTIONS.toArray(options);
//        this.optionsRowList = new GuiConfigButtonRowList(this.width, this.height, 32, this.height - 32, 25, options);
//        this.children.add(this.optionsRowList);
//    }
//
//    @Override
//    public IGuiEventListener getFocused()
//    {
//        return this.optionsRowList;
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_)
//    {
//        ExtendedConfig.save();
//        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
//    }
//
//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent)
//    {
//        int i = this.mc.gameSettings.guiScale;
//
//        if (super.mouseReleased(mouseX, mouseY, mouseEvent))
//        {
//            return true;
//        }
//        else if (this.optionsRowList.mouseReleased(mouseX, mouseY, mouseEvent))
//        {
//            if (this.mc.gameSettings.guiScale != i)
//            {
//                this.mc.mainWindow.updateSize();
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks)
//    {
//        this.drawDefaultBackground();
//        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
//        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.hypixel.title"), this.width / 2, 5, 16777215);
//        super.render(mouseX, mouseY, partialTicks);
//    }
//}