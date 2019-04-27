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
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import stevekung.mods.indicatia.config.ExtendedConfig;
//import stevekung.mods.stevekungslib.utils.LangUtils;
//
//@OnlyIn(Dist.CLIENT)
//public class GuiRenderInfoSettings extends GuiScreen
//{
//    private final GuiScreen parent;
//    private GuiConfigButtonRowList optionsRowList;
//    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
//
//    static
//    {
//        OPTIONS.add(ExtendedConfig.Options.FPS);
//        OPTIONS.add(ExtendedConfig.Options.XYZ);
//        OPTIONS.add(ExtendedConfig.Options.DIRECTION);
//        OPTIONS.add(ExtendedConfig.Options.BIOME);
//        OPTIONS.add(ExtendedConfig.Options.PING);
//        OPTIONS.add(ExtendedConfig.Options.PING_TO_SECOND);
//        OPTIONS.add(ExtendedConfig.Options.SERVER_IP);
//        OPTIONS.add(ExtendedConfig.Options.SERVER_IP_MC);
//        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_HUD);
//        OPTIONS.add(ExtendedConfig.Options.POTION_HUD);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_LRMB);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SS);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING);
//        OPTIONS.add(ExtendedConfig.Options.CPS);
//        OPTIONS.add(ExtendedConfig.Options.RCPS);
//        OPTIONS.add(ExtendedConfig.Options.SLIME_CHUNK);
//        OPTIONS.add(ExtendedConfig.Options.REAL_TIME);
//        OPTIONS.add(ExtendedConfig.Options.GAME_TIME);
//        OPTIONS.add(ExtendedConfig.Options.GAME_WEATHER);
//        OPTIONS.add(ExtendedConfig.Options.MOON_PHASE);
//        OPTIONS.add(ExtendedConfig.Options.POTION_ICON);
//        OPTIONS.add(ExtendedConfig.Options.TPS);
//        OPTIONS.add(ExtendedConfig.Options.TPS_ALL_DIMS);
//        OPTIONS.add(ExtendedConfig.Options.ALTERNATE_POTION_COLOR);
//    }
//
//    GuiRenderInfoSettings(GuiScreen parent)
//    {
//        this.parent = parent;
//    }
//
//    @Override
//    public void initGui()
//    {
//        this.addButton(new GuiButton(200, this.width / 2 - 100, this.height - 27, LangUtils.translate("gui.done"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                ExtendedConfig.save();
//                GuiRenderInfoSettings.this.mc.displayGuiScreen(GuiRenderInfoSettings.this.parent);
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
//        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.render_info.title"), this.width / 2, 5, 16777215);
//
//        if (GuiConfigButtonRowList.comment != null)
//        {
//            List<String> wrappedLine = this.fontRenderer.listFormattedStringToWidth(GuiConfigButtonRowList.comment, 250);
//            int y = 15;
//
//            for (String text : wrappedLine)
//            {
//                this.drawCenteredString(this.fontRenderer, TextFormatting.GREEN + text, this.width / 2, y, 16777215);
//                y += this.fontRenderer.FONT_HEIGHT;
//            }
//        }
//        else
//        {
//            this.drawCenteredString(this.fontRenderer, TextFormatting.YELLOW + LangUtils.translate("extended_config.render_info.rclick.info"), this.width / 2, 15, 16777215);
//        }
//        super.render(mouseX, mouseY, partialTicks);
//    }
//}