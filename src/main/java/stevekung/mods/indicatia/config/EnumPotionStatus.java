package stevekung.mods.indicatia.config;

public class EnumPotionStatus
{
    public enum Style
    {
        DEFAULT, ICON_AND_TIME;

        private static final Style[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }

    public enum Position
    {
        LEFT, RIGHT, HOTBAR_LEFT, HOTBAR_RIGHT;

        private static final Position[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }
}