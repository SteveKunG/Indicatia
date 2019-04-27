//package stevekung.mods.indicatia.gui.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.IGuiEventListener;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import stevekung.mods.indicatia.config.ExtendedConfig;
//import stevekung.mods.stevekungslib.utils.LangUtils;
//
//@OnlyIn(Dist.CLIENT)
//public class GuiOffsetSettings extends GuiScreen
//{
//    private final GuiScreen parent;
//    private GuiConfigButtonRowList optionsRowList;
//    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
//
//    static
//    {
//        OPTIONS.add(ExtendedConfig.Options.ARMOR_HUD_Y);
//        OPTIONS.add(ExtendedConfig.Options.POTION_HUD_Y);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_Y);
//        OPTIONS.add(ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY);
//        OPTIONS.add(ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET);
//        OPTIONS.add(ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP);
//    }
//
//    GuiOffsetSettings(GuiScreen parent)
//    {
//        this.parent = parent;
//    }
//
//    @Override
//    public void initGui()
//    {
//        this.addButton(new GuiButton(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                ExtendedConfig.save();
//                GuiOffsetSettings.this.mc.displayGuiScreen(GuiOffsetSettings.this.parent);
//            }
//        });
//        this.addButton(new GuiButton(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                ExtendedConfig.save();
//                GuiOffsetSettings.this.mc.displayGuiScreen(new GuiRenderPreview(GuiOffsetSettings.this, "offset"));
//            }
//        });
//
//        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
//        options = OPTIONS.toArray(options);
//        this.optionsRowList = new GuiConfigButtonRowList(this.width, this.height, 32, this.height - 32, 25, options);
//        this.children.add(this.optionsRowList);
//    }
//
//    @Nullable
//    @Override
//    public IGuiEventListener getFocused()
//    {
//        return this.optionsRowList;
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
//    {
//        ExtendedConfig.save();
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks)
//    {
//        this.drawDefaultBackground();
//        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
//        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.offset.title"), this.width / 2, 5, 16777215);
//        super.render(mouseX, mouseY, partialTicks);
//    }
//}