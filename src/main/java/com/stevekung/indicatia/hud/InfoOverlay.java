package com.stevekung.indicatia.hud;

import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;

public class InfoOverlay
{
    private String title;
    private String value;
    private String titleColor;
    private String valueColor;
    private Position pos;
    private boolean isEmpty;

    public InfoOverlay(String title, String value, String titleColor, String valueColor, Position pos)
    {
        this.title = title;
        this.value = value;
        this.titleColor = titleColor;
        this.valueColor = valueColor;
        this.pos = pos;
    }

    private InfoOverlay()
    {
        this.isEmpty = true;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getValue()
    {
        return this.value;
    }

    public String getTitleColor()
    {
        return this.titleColor;
    }

    public String getValueColor()
    {
        return this.valueColor;
    }

    public Position getPos()
    {
        return this.pos;
    }

    public boolean isEmpty()
    {
        return this.isEmpty;
    }

    public IFormattableTextComponent toFormatted()
    {
        IFormattableTextComponent formatted = new StringTextComponent("");
        IFormattableTextComponent title = LangUtils.translateComponent(this.title).deepCopy();

        String titleHex = String.format("#%02x%02x%02x", ColorUtils.stringToRGB(this.titleColor).red(), ColorUtils.stringToRGB(this.titleColor).green(), ColorUtils.stringToRGB(this.titleColor).blue());
        formatted.append(title.setStyle(title.getStyle().setColor(Color.func_240745_a_(titleHex))).appendString(": "));

        IFormattableTextComponent value = LangUtils.translateComponent(this.value).deepCopy();

        String valueHex = String.format("#%02x%02x%02x", ColorUtils.stringToRGB(this.valueColor).red(), ColorUtils.stringToRGB(this.valueColor).green(), ColorUtils.stringToRGB(this.valueColor).blue());
        formatted.append(value.setStyle(value.getStyle().setColor(Color.func_240745_a_(valueHex))));
        return this.isEmpty ? new StringTextComponent("") : formatted;
    }

    public static InfoOverlay empty()
    {
        return new InfoOverlay();
    }

    public enum Position
    {
        LEFT, RIGHT;
    }
}