package stevekung.mods.indicatia.util;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInputFromOptions;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;

@SideOnly(Side.CLIENT)
public class MovementInputFromOptionsIU extends MovementInputFromOptions
{
    private GameSettings gameSettings;
    private Minecraft mc;

    public MovementInputFromOptionsIU(GameSettings gameSettings)
    {
        super(gameSettings);
        this.gameSettings = gameSettings;
        this.mc = IndicatiaMod.MC;
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

            if (AutoLoginFunction.functionDelay == 0)
            {
                if (AutoLoginFunction.forwardTicks > 0 || AutoLoginFunction.forwardAfterCommandTicks > 0)
                {
                    this.moveForward++;
                }
            }

            if (this.gameSettings.keyBindForward.getIsKeyPressed())
            {
                ++this.moveForward;
            }
            if (this.gameSettings.keyBindBack.getIsKeyPressed() && !(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_S)))
            {
                --this.moveForward;
            }
            if (this.gameSettings.keyBindLeft.getIsKeyPressed() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_A)))
            {
                ++this.moveStrafe;
            }
            if (this.gameSettings.keyBindRight.getIsKeyPressed() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D)))
            {
                --this.moveStrafe;
            }
            if (ExtendedConfig.TOGGLE_SPRINT && !this.mc.thePlayer.isPotionActive(Potion.blindness) && !ExtendedConfig.TOGGLE_SNEAK)
            {
                this.mc.thePlayer.setSprinting(true);
            }

            if (this.mc.thePlayer.capabilities.isFlying && this.mc.thePlayer.ridingEntity == null)
            {
                this.mc.thePlayer.capabilities.setFlySpeed(0.05F * (this.mc.thePlayer.isSprinting() ? 2 : 1));
            }

            boolean swim = IndicatiaMod.isSteveKunG() && ExtendedConfig.AUTO_SWIM && (this.mc.thePlayer.isInWater() || this.mc.thePlayer.handleLavaMovement());
            this.jump = this.gameSettings.keyBindJump.getIsKeyPressed() || swim;
            this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed() || ExtendedConfig.TOGGLE_SNEAK;

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