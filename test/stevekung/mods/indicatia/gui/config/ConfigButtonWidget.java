package stevekung.mods.indicatia.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigButtonWidget extends ButtonWidget
{
    private static final List<String> SMALL_TEXT = new ArrayList<>();
    private final ExtendedConfig.Options options;
    private String comment;

    static
    {
        SMALL_TEXT.add("equipment.damage_and_max_damage");
        SMALL_TEXT.add("keystroke_mouse_button_rainbow.extended_config");
        SMALL_TEXT.add("keystroke_sprint_rainbow.extended_config");
        SMALL_TEXT.add("keystroke_sneak_rainbow.extended_config");
        SMALL_TEXT.add("keystroke_blocking_rainbow.extended_config");
        SMALL_TEXT.add("indicatia.hotbar_left");
        SMALL_TEXT.add("indicatia.hotbar_right");
    }

    public ConfigButtonWidget(int id, int x, int y, String text)
    {
        this(id, x, y, 150, null, text);
    }

    public ConfigButtonWidget(int id, int x, int y, int width, ExtendedConfig.Options options, String text)
    {
        super(id, x, y, width, 20, text);
        this.options = options;
    }

    public ConfigButtonWidget(int id, int x, int y, int width, ExtendedConfig.Options options, String text, String comment)
    {
        super(id, x, y, width, 20, text);
        this.options = options;
        this.comment = comment;
    }

    @Override
    public void onPressed(double mouseX, double mouseY)
    {
        ExtendedConfig.Options options = this.getOption();
        ExtendedConfig.instance.setOptionValue(options, 1);
        this.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
        ExtendedConfig.save();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (this.visible)
        {
            mc.getTextureManager().bindTexture(WIDGET_TEX);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int var6 = this.getTextureId(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcBlendFactor.ONE, GlStateManager.DstBlendFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedRect(this.x, this.y, 0, 46 + var6 * 20, this.width / 2, this.height);
            this.drawTexturedRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.drawBackground(mc, mouseX, mouseY);
            int color = 14737632;

            if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.hovered)
            {
                color = 16777120;
            }

            boolean smallText = SMALL_TEXT.stream().anyMatch(text -> this.text.trim().contains(LangUtils.translate(text)));

            if (smallText)
            {
//                mc.method_1568().setForceUnicodeFont(true);TODO
            }

            this.drawStringCentered(mc.fontRenderer, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (smallText)
            {
//                mc.method_1568().setForceUnicodeFont(mc.options.forceUnicodeFont);
            }
        }
    }

    public ExtendedConfig.Options getOption()
    {
        return this.options;
    }

    public String getComment()
    {
        return this.comment;
    }
}