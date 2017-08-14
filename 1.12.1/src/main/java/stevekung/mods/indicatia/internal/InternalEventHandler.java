package stevekung.mods.indicatia.internal;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.ModLogger;

public class InternalEventHandler
{
    public static boolean isTruefasterRainbow;
    public static boolean isInwTruefaster;
    public static int rainbowTimeStatic;
    public static int inwTimeStatic;
    private int rainbowTime;
    public static KeyBinding KEY_TRUEFASTER_RAINBOW;
    public static KeyBinding KEY_INW_TRUEFASTER;

    public static void init()
    {
        ModLogger.info("Initial SteveKunG fun stuff!");
        MinecraftForge.EVENT_BUS.register(new InternalEventHandler());
        InternalEventHandler.KEY_TRUEFASTER_RAINBOW = new KeyBinding("key.truefaster_rainbow.desc", Keyboard.KEY_6, "key.indicatia.category");
        InternalEventHandler.KEY_INW_TRUEFASTER = new KeyBinding("key.inw_truefaster.desc", Keyboard.KEY_7, "key.indicatia.category");
        ClientRegistry.registerKeyBinding(InternalEventHandler.KEY_TRUEFASTER_RAINBOW);
        ClientRegistry.registerKeyBinding(InternalEventHandler.KEY_INW_TRUEFASTER);
    }

    public static void renderRainbowArmor(Entity entity)
    {
        if (entity.getName().contains("truefaster") && InternalEventHandler.isTruefasterRainbow)
        {
            int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
            float red = (rainbow >> 16 & 255) / 255.0F;
            float green = (rainbow >> 8 & 255) / 255.0F;
            float blue = (rainbow & 255) / 255.0F;
            GlStateManager.color(red, green, blue);
        }
    }

    @SubscribeEvent
    public void onRegister(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().register(new SoundEvent(new ResourceLocation("indicatia:pete_music")).setRegistryName(new ResourceLocation("indicatia:pete_music")));
        event.getRegistry().register(new SoundEvent(new ResourceLocation("indicatia:inwtrue")).setRegistryName(new ResourceLocation("indicatia:inwtrue")));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            if (this.rainbowTime > 0)
            {
                this.rainbowTime--;
            }
            if (InternalEventHandler.inwTimeStatic < 260 && InternalEventHandler.isInwTruefaster)
            {
                InternalEventHandler.inwTimeStatic++;
            }
            if (InternalEventHandler.inwTimeStatic == 260)
            {
                InternalEventHandler.isInwTruefaster = false;
                InternalEventHandler.inwTimeStatic = 0;
            }
            if (this.rainbowTime == 0)
            {
                InternalEventHandler.isTruefasterRainbow = false;
                InternalEventHandler.rainbowTimeStatic = 0;
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event)
    {
        EntityLivingBase entity = event.getEntity();

        //GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);//XXX

        if (entity.getName().contains("truefaster") && this.rainbowTime > 0)
        {
            int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
            float red = (rainbow >> 16 & 255) / 255.0F;
            float green = (rainbow >> 8 & 255) / 255.0F;
            float blue = (rainbow & 255) / 255.0F;
            GlStateManager.color(red, green, blue);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        RenderPlayer renderDefault = IndicatiaMod.MC.getRenderManager().getSkinMap().get("default");
        renderDefault.addLayer(new LayerInwTruefaster());
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (InternalEventHandler.KEY_TRUEFASTER_RAINBOW.isKeyDown() && this.rainbowTime == 0)
        {
            IndicatiaMod.MC.player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("indicatia:pete_music")), 1.0F, 1.0F);
            InternalEventHandler.isTruefasterRainbow = true;
            this.rainbowTime = 280;
        }
        if (InternalEventHandler.KEY_INW_TRUEFASTER.isKeyDown() && InternalEventHandler.inwTimeStatic == 0)
        {
            IndicatiaMod.MC.player.playSound(SoundEvent.REGISTRY.getObject(new ResourceLocation("indicatia:inwtrue")), 1.0F, 1.0F);
            InternalEventHandler.isInwTruefaster = true;
        }
    }

}