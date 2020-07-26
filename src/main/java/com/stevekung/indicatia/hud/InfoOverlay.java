package com.stevekung.indicatia.hud;

import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

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

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ColorUtils.stringToRGB(this.titleColor).toColoredFont());
        builder.append(LangUtils.translateComponent(this.title).getString());
        builder.append(": ");
        builder.append(ColorUtils.stringToRGB(this.valueColor).toColoredFont());
        builder.append(LangUtils.translateComponent(this.value).getString());
        return this.isEmpty ? "" : builder.toString();
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