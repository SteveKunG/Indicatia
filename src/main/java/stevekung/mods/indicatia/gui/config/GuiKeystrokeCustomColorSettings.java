//package stevekung.mods.indicatia.gui.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.IGuiEventListener;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import stevekung.mods.indicatia.config.ExtendedConfig;
//import stevekung.mods.stevekungslib.utils.ColorUtils;
//import stevekung.mods.stevekungslib.utils.ColorUtils.RGB;
//import stevekung.mods.stevekungslib.utils.LangUtils;
//
//@OnlyIn(Dist.CLIENT)
//public class GuiKeystrokeCustomColorSettings extends GuiScreen
//{
//    private final GuiScreen parent;
//    private GuiConfigTextFieldRowList optionsRowList;
//    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
//
//    static
//    {
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_WASD_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SPRINT_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SNEAK_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_CPS_COLOR);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_RCPS_COLOR);
//
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_WASD_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SPRINT_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SNEAK_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_CPS_RAINBOW);
//        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_RCPS_RAINBOW);
//    }
//
//    GuiKeystrokeCustomColorSettings(GuiScreen parent)
//    {
//        this.parent = parent;
//    }
//
//    @Override
//    public void initGui()
//    {
//        this.mc.keyboardListener.enableRepeatEvents(true);
//        this.addButton(new GuiButton(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                GuiKeystrokeCustomColorSettings.this.optionsRowList.saveCurrentValue();
//                ExtendedConfig.save();
//                GuiKeystrokeCustomColorSettings.this.mc.displayGuiScreen(GuiKeystrokeCustomColorSettings.this.parent);
//            }
//        });
//        this.addButton(new GuiButton(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"))
//        {
//            @Override
//            public void onClick(double mouseX, double mouseZ)
//            {
//                GuiKeystrokeCustomColorSettings.this.optionsRowList.saveCurrentValue();
//                ExtendedConfig.save();
//                GuiKeystrokeCustomColorSettings.this.mc.displayGuiScreen(new GuiRenderPreview(GuiKeystrokeCustomColorSettings.this, "keystroke"));
//            }
//        });
//
//        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
//        options = OPTIONS.toArray(options);
//        this.optionsRowList = new GuiConfigTextFieldRowList(this.width, this.height, 32, this.height - 32, 25, options);
//        this.children.add(this.optionsRowList);
//        this.children.addAll(this.optionsRowList.getTextField());
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
//    public void onGuiClosed()
//    {
//        this.mc.keyboardListener.enableRepeatEvents(false);
//    }
//
//    @Override
//    public void tick()
//    {
//        this.optionsRowList.tick();
//    }
//
//    @Override
//    public void onResize(Minecraft mc, int width, int height)
//    {
//        this.optionsRowList.onResize();
//        super.onResize(mc, width, height);
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
//    {
//        ExtendedConfig.save();
//        this.optionsRowList.saveCurrentValue();
//        this.optionsRowList.keyPressedText(keyCode, scanCode, modifiers);
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    @Override
//    public boolean charTyped(char codePoint, int modifiers)
//    {
//        this.optionsRowList.charTypedText(codePoint, modifiers);
//        return super.charTyped(codePoint, modifiers);
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks)
//    {
//        this.drawDefaultBackground();
//        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
//        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.keystroke_custom_color.title"), this.width / 2, 5, 16777215);
//        int index = this.optionsRowList.selected;
//
//        if (index != -1)
//        {
//            ExtendedConfig.Options options = this.optionsRowList.getChildren().get(index).getTextField().getOption();
//            RGB rgb = ColorUtils.stringToRGB(this.optionsRowList.getChildren().get(index).getTextField().getText());
//            this.drawCenteredString(ColorUtils.coloredFontRenderer, LangUtils.translate("menu.example") + ": " + rgb.toColoredFont() + options.getTranslation(), this.width / 2, 15, 16777215);
//        }
//        else
//        {
//            this.drawCenteredString(this.fontRenderer, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
//        }
//        super.render(mouseX, mouseY, partialTicks);
//    }
//}