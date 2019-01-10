package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class GuiConfigButton extends GuiButton
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

    GuiConfigButton(int id, int x, int y, int width, ExtendedConfig.Options options, String text)
    {
        super(id, x, y, width, 20, text);
        this.options = options;
    }

    GuiConfigButton(int id, int x, int y, int width, ExtendedConfig.Options options, String text, String comment)
    {
        super(id, x, y, width, 20, text);
        this.options = options;
        this.comment = comment;
    }

    @Override
    public void onClick(double mouseX, double mouseZ)
    {
        ExtendedConfig.save();
        ExtendedConfig.instance.setOptionValue(this.options, 1);
        this.displayString = ExtendedConfig.instance.getKeyBinding(this.id == 150 ? ExtendedConfig.Options.PREVIEW : ExtendedConfig.Options.byOrdinal(this.id));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + state * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
            this.renderBg(mc, mouseX, mouseY);
            int color = 14737632;

            if (this.packedFGColor != 0)
            {
                color = this.packedFGColor;
            }
            else
            {
                if (!this.enabled)
                {
                    color = 10526880;
                }
                else if (this.hovered)
                {
                    color = 16777120;
                }
            }

            boolean smallText = SMALL_TEXT.stream().anyMatch(text -> this.displayString.trim().contains(LangUtils.translate(text)));

            if (smallText)
            {
                //                mc.fontRenderer.setUnicodeFlag(true);TODO
            }

            this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (smallText)
            {
                //                mc.fontRenderer.setUnicodeFlag(mc.getLanguageManager().isCurrentLocaleUnicode() || mc.gameSettings.forceUnicodeFont);
            }
        }
    }

    public ExtendedConfig.Options getOption()
    {
        return this.options;
    }

    String getComment()
    {
        return this.comment;
    }
}