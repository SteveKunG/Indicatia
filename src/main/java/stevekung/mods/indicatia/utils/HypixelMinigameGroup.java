package stevekung.mods.indicatia.utils;

public enum HypixelMinigameGroup
{
    MAIN("Main"),
    ARCADE("Arcade Games"),
    BEDWARS("Bedwars"),
    BUILD_BATTLE("Build Battle"),
    CLASSIC_GAMES("Classic Games"),
    COPS_AND_CRIMS("Cops and Crims"),
    CRAZY_WALLS("Crazy Walls"),
    DUELS_SOLO("Duels: Solo"),
    DUELS_DOUBLES("Duels: Doubles"),
    MEGA_WALLS("Mega Walls"),
    MURDER_MYSTERY("Murder Mystery"),
    SKYCLASH("Skyclash"),
    SKYWARS("Skywars"),
    SKYWARS_LAB("Skywars Lab"),
    SMASH_HEROES("Smash Heroes"),
    SPEED_UHC("Speed UHC"),
    SURVIVAL_GAMES("Blitz Survival Games"),
    TNT("TNT Games"),
    UHC_CHAMPIONS("UHC Champions"),
    WARLORDS("Warlords"),
    PROTOTYPE_CAPTURE_THE_WOOL("Prototype: Capture The Wool"),
    PROTOTYPE_KOTH("Prototype: King of the Hills"),
    PROTOTYPE_THE_BRIDGE("Prototype: The Bridge"),
    ;

    public static final HypixelMinigameGroup[] values = HypixelMinigameGroup.values();

    private String name;

    HypixelMinigameGroup(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return this.name().toLowerCase();
    }
}