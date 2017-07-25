package stevekung.mods.indicatia.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatia.command.*;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.handler.*;
import stevekung.mods.indicatia.profile.RenderProfileConfig;
import stevekung.mods.indicatia.renderer.RenderPlayerNew;
import stevekung.mods.indicatia.utils.*;

@Mod(modid = IndicatiaMod.MOD_ID, name = IndicatiaMod.NAME, version = IndicatiaMod.VERSION, dependencies = IndicatiaMod.FORGE_VERSION, guiFactory = IndicatiaMod.GUI_FACTORY)
public class IndicatiaMod
{
    public static final String NAME = "Indicatia";
    public static final String MOD_ID = "indicatia";
    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;
    public static final int BUILD_VERSION = 5;
    public static final String VERSION = IndicatiaMod.MAJOR_VERSION + "." + IndicatiaMod.MINOR_VERSION + "." + IndicatiaMod.BUILD_VERSION;
    public static final String MC_VERSION = String.valueOf(FMLInjectionData.data()[4]);
    public static final String GUI_FACTORY = "stevekung.mods.indicatia.config.ConfigGuiFactory";
    public static final String FORGE_VERSION = "after:Forge@[10.13.4.1614,);";
    public static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    private static boolean DEOBFUSCATED;
    public static Minecraft MC;
    public static boolean CHECK_NO_CONNECTION;
    public static boolean SHOW_ANNOUNCE_MESSAGE;
    public static boolean FOUND_LATEST;

    static
    {
        try
        {
            IndicatiaMod.DEOBFUSCATED = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (Exception e) {}

        IndicatiaMod.MC = Minecraft.getMinecraft();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        IndicatiaMod.overrideClientCommandHandler();
        IndicatiaMod.init(event.getModMetadata());
        ConfigManager.init(new File(event.getModConfigurationDirectory(), "indicatia.cfg"));
        KeyBindingHandler.init();
        ExtendedConfig.load();
        RenderProfileConfig.load();

        IndicatiaMod.registerForgeEvent(new HUDRenderHandler(IndicatiaMod.MC));
        IndicatiaMod.registerForgeEvent(new CommonHandler(IndicatiaMod.MC));
        IndicatiaMod.registerForgeEvent(new PlayerChatHandler(IndicatiaMod.MC));
        IndicatiaMod.registerForgeEvent(new CapeRendererHandler());

        ClientCommandHandler.instance.registerCommand(new CommandMojangStatusCheck());
        ClientCommandHandler.instance.registerCommand(new CommandChangeLog());
        ClientCommandHandler.instance.registerCommand(new CommandAutoLogin());
        ClientCommandHandler.instance.registerCommand(new CommandSlimeChunkSeed());
        ClientCommandHandler.instance.registerCommand(new CommandAFK());
        ClientCommandHandler.instance.registerCommand(new CommandIndicatia());
        ClientCommandHandler.instance.registerCommand(new CommandProfile());

        if (IndicatiaMod.isSteveKunG())
        {
            ClientCommandHandler.instance.registerCommand(new CommandAutoClick());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (ConfigManager.enableAlternatePlayerModel)
        {
            RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSP.class, new RenderPlayerNew());
            RenderingRegistry.registerEntityRenderingHandler(EntityOtherPlayerMP.class, new RenderPlayerNew());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (ConfigManager.enableVersionChecker)
        {
            VersionChecker.startCheck();
        }
        CapeUtils.loadCapeTextureAtStartup();
    }

    public static boolean isObfuscatedEnvironment()
    {
        return IndicatiaMod.DEOBFUSCATED;
    }

    public static boolean isSteveKunG()
    {
        return GameProfileUtil.getUsername().equals("SteveKunG") && GameProfileUtil.getUUID().equals(UUID.fromString("eef3a603-1c1b-4c98-8264-d2f04b231ef4")) || IndicatiaMod.isObfuscatedEnvironment();
    }

    public static void registerForgeEvent(Object obj)
    {
        MinecraftForge.EVENT_BUS.register(obj);
        FMLCommonHandler.instance().bus().register(obj);
    }

    public static void unregisterForgeEvent(Object obj)
    {
        MinecraftForge.EVENT_BUS.unregister(obj);
        FMLCommonHandler.instance().bus().unregister(obj);
    }

    private static void init(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = IndicatiaMod.MOD_ID;
        info.name = IndicatiaMod.NAME;
        info.version = IndicatiaMod.VERSION;
        info.description = "Simple in-game info and utility!";
        info.url = IndicatiaMod.URL;
        info.authorList = Arrays.asList("SteveKunG");
    }

    private static void overrideClientCommandHandler()
    {
        try
        {
            Field field = ClientCommandHandler.class.getDeclaredField("instance");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & -17);
            field.set(ClientCommandHandler.class.cast(ClientCommandHandler.instance), new CCMDHandler());
            ModLogger.info("Successfully replacing {} instance", ClientCommandHandler.class.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ModLogger.error("Couldn't replace {} instance!", ClientCommandHandler.class.getName());
        }
    }
}