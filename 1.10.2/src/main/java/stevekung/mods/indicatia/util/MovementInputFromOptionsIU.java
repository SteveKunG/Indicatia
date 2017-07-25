package stevekung.mods.indicatia.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;
import stevekung.mods.indicatia.handler.KeyBindingHandler;

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
                this.forwardKeyDown = true;
            }
            else if (afkMoveTick > 2 && afkMoveTick < 4)
            {
                ++this.moveStrafe;
                this.leftKeyDown = true;
            }
            else if (afkMoveTick > 4 && afkMoveTick < 6)
            {
                --this.moveForward;
                this.backKeyDown = true;
            }
            else if (afkMoveTick > 6 && afkMoveTick < 8)
            {
                --this.moveStrafe;
                this.rightKeyDown = true;
            }

            if (this.gameSettings.keyBindForward.isKeyDown())
            {
                ++this.moveForward;
                this.forwardKeyDown = true;
            }
            else
            {
                this.forwardKeyDown = false;
            }

            if (this.gameSettings.keyBindBack.isKeyDown() && !(KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown() || KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown()))
            {
                --this.moveForward;
                this.backKeyDown = true;
            }
            else
            {
                this.backKeyDown = false;
            }

            if (this.gameSettings.keyBindLeft.isKeyDown())
            {
                ++this.moveStrafe;
                this.leftKeyDown = true;
            }
            else
            {
                this.leftKeyDown = false;
            }

            if (this.gameSettings.keyBindRight.isKeyDown())
            {
                --this.moveStrafe;
                this.rightKeyDown = true;
            }
            else
            {
                this.rightKeyDown = false;
            }

            boolean swim = IndicatiaMod.isSteveKunG() && ExtendedConfig.AUTO_SWIM && (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava()) && !this.mc.thePlayer.isSpectator();
            this.jump = this.gameSettings.keyBindJump.isKeyDown() || swim;
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown() || ExtendedConfig.TOGGLE_SNEAK;

            if (ExtendedConfig.TOGGLE_SPRINT && !this.mc.thePlayer.isPotionActive(MobEffects.BLINDNESS) && !ExtendedConfig.TOGGLE_SNEAK)
            {
                this.mc.thePlayer.setSprinting(true);
            }

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