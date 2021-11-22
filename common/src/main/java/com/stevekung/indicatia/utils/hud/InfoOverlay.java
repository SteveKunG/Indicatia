package com.stevekung.indicatia.utils.hud;

import com.stevekung.stevekunglib.utils.ColorUtils;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

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

    public Position getPos()
    {
        return this.pos;
    }

    public boolean isEmpty()
    {
        return this.isEmpty;
    }

    public MutableComponent toFormatted()
    {
        var formatted = TextComponent.EMPTY.copy();
        var title = LangUtils.translate(this.title).copy();

        formatted.append(title.setStyle(title.getStyle().withColor(TextColor.fromRgb(ColorUtils.rgbToDecimal(this.titleColor.replaceAll("\\s+", ""))))).append(": "));

        var value = LangUtils.translate(this.value).copy();

        formatted.append(value.setStyle(value.getStyle().withColor(TextColor.fromRgb(ColorUtils.rgbToDecimal(this.valueColor.replaceAll("\\s+", ""))))));
        return this.isEmpty ? TextComponent.EMPTY.copy() : formatted;
    }

    public static InfoOverlay empty()
    {
        return new InfoOverlay();
    }

    public enum Position
    {
        LEFT,
        RIGHT
    }
}