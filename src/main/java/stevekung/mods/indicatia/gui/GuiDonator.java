package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.enums.CachedEnum;

public class GuiDonator extends GuiScreen
{
    private GuiTextField topDonateInput;
    private GuiTextField recentDonateInput;
    private GuiTextField topDonateTextInput;
    private GuiTextField recentDonateTextInput;
    private GuiButton resetBtn;

    @Override
    public void initGui()
    {
        this.mc.keyboardListener.enableRepeatEvents(true);
        this.topDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 60, 220, 20);
        this.topDonateInput.setMaxStringLength(32767);
        this.topDonateInput.setCanLoseFocus(true);
        this.topDonateInput.setText(ExtendedConfig.topDonatorFilePath);

        this.recentDonateInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 85, 220, 20);
        this.recentDonateInput.setMaxStringLength(32767);
        this.recentDonateInput.setCanLoseFocus(true);
        this.recentDonateInput.setText(ExtendedConfig.recentDonatorFilePath);

        this.topDonateTextInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 110, 220, 20);
        this.topDonateTextInput.setMaxStringLength(32767);
        this.topDonateTextInput.setCanLoseFocus(true);
        this.topDonateTextInput.setText(ExtendedConfig.topDonatorText.replace("\u00a7", "&"));

        this.recentDonateTextInput = new GuiTextField(2, this.fontRenderer, this.width / 2 - 80, 135, 220, 20);
        this.recentDonateTextInput.setMaxStringLength(32767);
        this.recentDonateTextInput.setCanLoseFocus(true);
        this.recentDonateTextInput.setText(ExtendedConfig.recentDonatorText.replace("\u00a7", "&"));

        this.addButton(new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height - 38, 100, 20, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                if (!ExtendedConfig.topDonatorFilePath.equals(GuiDonator.this.topDonateInput.getText()))
                {
                    GuiDonator.this.mc.player.sendMessage(JsonUtils.create("Set top donator file path to " + GuiDonator.this.topDonateInput.getText()));
                }
                if (!ExtendedConfig.recentDonatorFilePath.equals(GuiDonator.this.recentDonateInput.getText()))
                {
                    GuiDonator.this.mc.player.sendMessage(JsonUtils.create("Set recent donator file path to " + GuiDonator.this.recentDonateInput.getText()));
                }
                ExtendedConfig.topDonatorFilePath = GuiDonator.this.topDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.recentDonatorFilePath = GuiDonator.this.recentDonateInput.getText().replace("" + '\u0022', "");
                ExtendedConfig.topDonatorText = GuiDonator.this.convertString(GuiDonator.this.topDonateTextInput.getText());
                ExtendedConfig.recentDonatorText = GuiDonator.this.convertString(GuiDonator.this.recentDonateTextInput.getText());
                ExtendedConfig.save();
                GuiDonator.this.mc.displayGuiScreen(null);
            }
        });
        this.addButton(new GuiButton(1, this.width / 2 + 50 + 4, this.height - 38, 100, 20, LangUtils.translate("gui.cancel"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiDonator.this.mc.displayGuiScreen(null);
            }
        });
        this.resetBtn = this.addButton(new GuiButton(2, this.width / 2 - 50, this.height - 38, 100, 20, LangUtils.translate("message.reset_path"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiDonator.this.mc.player.sendMessage(JsonUtils.create(LangUtils.translate("message.reset_donator_file_path")));
                ExtendedConfig.topDonatorFilePath = "";
                ExtendedConfig.recentDonatorFilePath = "";
                HUDRenderEventHandler.topDonator = "";
                HUDRenderEventHandler.recentDonator = "";
                GuiDonator.this.topDonateInput.setText("");
                GuiDonator.this.recentDonateInput.setText("");
                ExtendedConfig.save();
            }
        });
        this.resetBtn.enabled = !ExtendedConfig.topDonatorFilePath.isEmpty() || !ExtendedConfig.recentDonatorFilePath.isEmpty();
    }

    @Override
    public void tick()
    {
        this.resetBtn.enabled = !ExtendedConfig.topDonatorFilePath.isEmpty() || !ExtendedConfig.recentDonatorFilePath.isEmpty();
        this.topDonateInput.tick();
        this.recentDonateInput.tick();
        this.topDonateTextInput.tick();
        this.recentDonateTextInput.tick();
    }

    @Override
    public void onGuiClosed()
    {
        this.mc.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        this.topDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.topDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
        this.recentDonateTextInput.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Donator Message Settings", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRenderer, "Put your twitch donators file path. (.txt file only)", this.width / 2, 37, 10526880);
        this.drawString(this.fontRenderer, "Top Donate:", this.width / 2 - 145, 66, 10526880);
        this.drawString(this.fontRenderer, "Recent Donate:", this.width / 2 - 160, 90, 10526880);
        this.drawString(this.fontRenderer, "Top Donate Text:", this.width / 2 - 170, 115, 10526880);
        this.drawString(this.fontRenderer, "Recent Donate Text:", this.width / 2 - 185, 140, 10526880);
        this.drawCenteredString(this.fontRenderer, TextFormatting.RESET + "Top Donate Text: " + this.convertString(this.topDonateTextInput.getText()), this.width / 2, 170, 10526880);
        this.drawCenteredString(this.fontRenderer, TextFormatting.RESET + "Recent Donate Text: " + this.convertString(this.recentDonateTextInput.getText()), this.width / 2, 185, 10526880);
        this.topDonateInput.drawTextField(mouseX, mouseY, partialTicks);
        this.recentDonateInput.drawTextField(mouseX, mouseY, partialTicks);
        this.topDonateTextInput.drawTextField(mouseX, mouseY, partialTicks);
        this.recentDonateTextInput.drawTextField(mouseX, mouseY, partialTicks);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private String convertString(String original)
    {
        for (TextFormatting formatting : CachedEnum.textFormatValues)
        {
            if (original.contains("&" + formatting.formattingCode))
            {
                original = original.replace("&" + formatting.formattingCode, "\u00a7" + formatting.formattingCode);
            }
        }
        return original;
    }
}