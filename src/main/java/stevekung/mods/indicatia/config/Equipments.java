package stevekung.mods.indicatia.config;

public class Equipments
{
    public enum Ordering
    {
        DEFAULT, REVERSE;

        private static final Ordering[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }

    public enum Direction
    {
        VERTICAL, HORIZONTAL;

        private static final Direction[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }

    public enum Status
    {
        DAMAGE_AND_MAX_DAMAGE, PERCENT, ONLY_DAMAGE, NONE;

        private static final Status[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }

    public enum Position
    {
        LEFT, RIGHT, HOTBAR;

        private static final Position[] values = values();

        public static String getById(int mode)
        {
            return values[mode].toString().toLowerCase();
        }
    }
}