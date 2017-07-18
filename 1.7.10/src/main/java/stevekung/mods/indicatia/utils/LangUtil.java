package stevekung.mods.indicatia.utils;

import net.minecraft.util.StringTranslate;

public class LangUtil
{
    private static final StringTranslate INSTANCE = StringTranslate.getInstance();

    public static String translate(String key)
    {
        return LangUtil.INSTANCE.translateKey(key);
    }

    public static String translate(String key, Object... format)
    {
        return LangUtil.INSTANCE.translateKeyFormat(key, format);
    }
}