package com.stevekung.indicatia.utils.hud;

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
        IFormattableTextComponent formatted = StringTextComponent.EMPTY.deepCopy();
        IFormattableTextComponent title = LangUtils.translate(this.title).deepCopy();

        formatted.appendSibling(title.setStyle(title.getStyle().setColor(Color.fromInt(ColorUtils.rgbToDecimal(this.titleColor)))).appendString(": "));

        IFormattableTextComponent value = LangUtils.translate(this.value).deepCopy();

        formatted.appendSibling(value.setStyle(value.getStyle().setColor(Color.fromInt(ColorUtils.rgbToDecimal(this.valueColor)))));
        return this.isEmpty ? StringTextComponent.EMPTY.deepCopy() : formatted;
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