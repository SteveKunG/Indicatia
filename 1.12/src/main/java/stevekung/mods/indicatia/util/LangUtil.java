package stevekung.mods.indicatia.util;

import net.minecraft.util.text.translation.LanguageMap;

public class LangUtil
{
    private static final LanguageMap INSTANCE = LanguageMap.getInstance();

    public static String translate(String key)
    {
        return LangUtil.INSTANCE.translateKey(key);
    }
}