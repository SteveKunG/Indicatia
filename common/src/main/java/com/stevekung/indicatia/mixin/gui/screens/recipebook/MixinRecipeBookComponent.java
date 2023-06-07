package com.stevekung.indicatia.mixin.gui.screens.recipebook;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.PlatformKeyInput;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookComponent
{
    @Shadow
    EditBox searchBox;

    @Shadow
    String lastSearch;

    @Shadow
    abstract void updateCollections(boolean bl);

    private static String lastSearchStatic = "";

    @Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.setValue(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void indicatia$initStaticLastSearch(CallbackInfo info)
    {
        if (Indicatia.CONFIG.savedLastSearchInRecipeBook)
        {
            this.searchBox.setValue(lastSearchStatic);
        }
    }

    @Inject(method = "checkSearchStringUpdate", at = @At(value = "FIELD", target = "net/minecraft/client/gui/screens/recipebook/RecipeBookComponent.lastSearch:Ljava/lang/String;", opcode = Opcodes.PUTFIELD), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$staticLastSearchUpdate(CallbackInfo info, String string)
    {
        if (Indicatia.CONFIG.savedLastSearchInRecipeBook)
        {
            lastSearchStatic = string;
        }
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.mouseClicked(DDI)Z", ordinal = 0))
    private boolean indicatia$rightClickClearText(EditBox editBox, double mouseX, double mouseY, int button)
    {
        var inBox = mouseX >= (double) editBox.getX() && mouseX < (double) (editBox.getX() + editBox.getWidth()) && mouseY >= (double) editBox.getY() && mouseY < (double) (editBox.getY() + editBox.getHeight());

        if (Indicatia.CONFIG.savedLastSearchInRecipeBook && !this.searchBox.getValue().isEmpty() && inBox && button == 1)
        {
            this.searchBox.setValue("");
            this.lastSearch = "";
            lastSearchStatic = "";
            this.updateCollections(false);
        }
        return editBox.mouseClicked(mouseX, mouseY, button);
    }

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "net/minecraft/client/KeyMapping.matches(II)Z"))
    private boolean indicatia$addAltChatKey(KeyMapping key, int keysym, int scancode)
    {
        return key.matches(keysym, scancode) || PlatformKeyInput.isAltChatMatches(keysym, scancode);
    }
}