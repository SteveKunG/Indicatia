package stevekung.mods.indicatia.gui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.Charsets;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.utils.LangUtil;

@SideOnly(Side.CLIENT)
public class GuiFullChangeLog extends GuiScreen
{
    private List<String> stringList;
    private GuiChangeLogSlot changeLogSlot;
    private Random rand;

    public void display()
    {
        IndicatiaMod.registerForgeEvent(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        Minecraft.getMinecraft().displayGuiScreen(this);
        IndicatiaMod.unregisterForgeEvent(this);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 120, LangUtil.translate("gui.done")));

        if (this.stringList == null)
        {
            this.stringList = Lists.newArrayList();

            try
            {
                String s = "";
                InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("indicatia:change_log.txt")).getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));

                while ((s = bufferedreader.readLine()) != null)
                {
                    s = s.replaceAll("-Added-", EnumChatFormatting.GREEN + "+" + EnumChatFormatting.RESET);
                    s = s.replaceAll("-Remove-", EnumChatFormatting.RED + "-" + EnumChatFormatting.RESET);
                    s = s.replaceAll("-Fixed-", EnumChatFormatting.GOLD + "*" + EnumChatFormatting.RESET);
                    s = s.replaceAll("-Update-", EnumChatFormatting.YELLOW + "*" + EnumChatFormatting.RESET);
                    this.stringList.addAll(this.mc.fontRenderer.listFormattedStringToWidth(s, 264));
                    this.rand = new Random();
                }
                inputstream.close();
            }
            catch (Exception e) {}
        }
        this.changeLogSlot = new GuiChangeLogSlot(this.mc, this, this.stringList, this.width, this.height, this.rand.nextBoolean());
        this.changeLogSlot.registerScrollButtons(1, 1);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1 || keyCode == 28)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.changeLogSlot != null)
        {
            this.changeLogSlot.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}