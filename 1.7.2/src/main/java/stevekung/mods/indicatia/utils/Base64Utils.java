package stevekung.mods.indicatia.utils;

import javax.xml.bind.DatatypeConverter;

public class Base64Utils
{
    public static String decode(String string)
    {
        return new String(DatatypeConverter.parseBase64Binary(string));
    }

    public static String encode(String string)
    {
        return DatatypeConverter.printBase64Binary(string.getBytes());
    }
}