package stevekung.mods.indicatia.util;

import java.io.BufferedReader;
import java.io.StringReader;

import net.minecraft.client.Minecraft;

public class AutoLoginFunction
{
    public static String functionValue = "";
    public static boolean run;

    public static int functionDelay;
    public static int rightClickDelay;
    public static int forwardTicks;
    public static int forwardAfterCommandTicks;
    public static int commandDelayTicks;
    public static boolean useRightClick;
    public static boolean useSprint;
    public static boolean useRotation;
    public static boolean runAfterCmd;
    public static String command = "";
    public static float pitch;
    public static float yaw;

    public static void runAutoLoginFunction()
    {
        if (AutoLoginFunction.run && !AutoLoginFunction.functionValue.isEmpty())
        {
            String[] functionList = AutoLoginFunction.functionValue.split(",");
            BufferedReader buffer = new BufferedReader(new StringReader(String.join("\n", functionList)));
            String data = "";

            try
            {
                while ((data = buffer.readLine()) != null)
                {
                    String[] dataList = data.split(":");
                    String option = dataList[0];
                    String value = dataList[1];

                    if (option.equals("command"))
                    {
                        value = value.replace("$space", "\u0020");
                        AutoLoginFunction.command = value;
                    }
                    if (option.equals("command_delay"))
                    {
                        try
                        {
                            AutoLoginFunction.commandDelayTicks = Integer.parseInt(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("forward"))
                    {
                        try
                        {
                            AutoLoginFunction.forwardTicks = Integer.parseInt(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("forward_after"))
                    {
                        try
                        {
                            AutoLoginFunction.forwardAfterCommandTicks = Integer.parseInt(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("sprint"))
                    {
                        try
                        {
                            AutoLoginFunction.useSprint = Boolean.valueOf(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("right_click"))
                    {
                        try
                        {
                            AutoLoginFunction.useRightClick = Boolean.valueOf(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("right_click_delay"))
                    {
                        try
                        {
                            AutoLoginFunction.rightClickDelay = Integer.parseInt(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("function_delay"))
                    {
                        try
                        {
                            AutoLoginFunction.functionDelay = Integer.parseInt(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("rotation"))
                    {
                        try
                        {
                            AutoLoginFunction.useRotation = Boolean.valueOf(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("pitch"))
                    {
                        try
                        {
                            AutoLoginFunction.pitch = Float.parseFloat(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (option.equals("yaw"))
                    {
                        try
                        {
                            AutoLoginFunction.yaw = Float.parseFloat(value);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception e) {}
            AutoLoginFunction.run = false;
        }
    }

    public static void runAutoLoginFunctionTicks(Minecraft mc)
    {
        if (AutoLoginFunction.forwardTicks > 0)
        {
            AutoLoginFunction.forwardTicks--;

            if (AutoLoginFunction.forwardTicks == 0)
            {
                AutoLoginFunction.useRightClick = false;
                AutoLoginFunction.useSprint = false;
            }
        }

        if (AutoLoginFunction.runAfterCmd)
        {
            if (AutoLoginFunction.forwardAfterCommandTicks > 0)
            {
                AutoLoginFunction.forwardAfterCommandTicks--;

                if (AutoLoginFunction.forwardAfterCommandTicks == 0)
                {
                    AutoLoginFunction.forwardTicks = -1;
                    AutoLoginFunction.runAfterCmd = false;
                }
            }
        }

        if (AutoLoginFunction.useRotation)
        {
            mc.thePlayer.rotationPitch = AutoLoginFunction.pitch;
            mc.thePlayer.rotationYaw = AutoLoginFunction.yaw;
            AutoLoginFunction.useRotation = false;
        }
        if (AutoLoginFunction.useRightClick)
        {
            if (AutoLoginFunction.rightClickDelay > 0)
            {
                AutoLoginFunction.rightClickDelay--;
            }
            if (AutoLoginFunction.rightClickDelay == 0 && mc.thePlayer.getCurrentEquippedItem() == null)
            {
                mc.rightClickMouse();
            }
        }
        if (AutoLoginFunction.useSprint)
        {
            mc.thePlayer.setSprinting(true);
        }

        if (AutoLoginFunction.commandDelayTicks > 0)
        {
            AutoLoginFunction.commandDelayTicks--;

            if (AutoLoginFunction.commandDelayTicks == 0)
            {
                mc.thePlayer.sendChatMessage(AutoLoginFunction.command);
                AutoLoginFunction.commandDelayTicks = -1;
                AutoLoginFunction.runAfterCmd = true;
            }
        }
    }
}