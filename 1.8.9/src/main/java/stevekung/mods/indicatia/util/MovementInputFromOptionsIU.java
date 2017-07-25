package stevekung.mods.indicatia.util;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;

@SideOnly(Side.CLIENT)
public class MovementInputFromOptionsIU extends MovementInputFromOptions
{
    private GameSettings gameSettings;
    private final Minecraft mc = IndicatiaMod.MC;

    public MovementInputFromOptionsIU(GameSettings gameSettings)
    {
        super(gameSettings);
        this.gameSettings = gameSettings;
    }

    @Override
    public void updatePlayerMoveState()
    {
        if (ConfigManager.enableCustomMovementHandler)
        {
            this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            int afkMoveTick = CommonHandler.afkMoveTicks;

            if (afkMoveTick > 0 && afkMoveTick < 2)
            {
                ++this.moveForward;
            }
            else if (afkMoveTick > 2 && afkMoveTick < 4)
            {
                ++this.moveStrafe;
            }
            else if (afkMoveTick > 4 && afkMoveTick < 6)
            {
                --this.moveForward;
            }
            else if (afkMoveTick > 6 && afkMoveTick < 8)
            {
                --this.moveStrafe;
            }

            if (this.gameSettings.keyBindForward.isKeyDown())
            {
                ++this.moveForward;
            }
            if (this.gameSettings.keyBindBack.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_S)))
            {
                --this.moveForward;
            }
            if (this.gameSettings.keyBindLeft.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_A)))
            {
                ++this.moveStrafe;
            }
            if (this.gameSettings.keyBindRight.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D)))
            {
                --this.moveStrafe;
            }
            if (ExtendedConfig.TOGGLE_SPRINT && !this.mc.thePlayer.isPotionActive(Potion.blindness) && !ExtendedConfig.TOGGLE_SNEAK)
            {
                this.mc.thePlayer.setSprinting(true);
            }

            boolean swim = IndicatiaMod.isSteveKunG() && ExtendedConfig.AUTO_SWIM && (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava()) && !this.mc.thePlayer.isSpectator();
            this.jump = this.gameSettings.keyBindJump.isKeyDown() || swim;
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown() || ExtendedConfig.TOGGLE_SNEAK;

            if (this.sneak)
            {
                this.moveStrafe = (float)(this.moveStrafe * 0.3D);
                this.moveForward = (float)(this.moveForward * 0.3D);
            }
        }
        else
        {
            super.updatePlayerMoveState();
        }
    }
}