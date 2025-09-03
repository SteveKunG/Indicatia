package com.stevekung.indicatia.mixin.gui.screens.recipebook;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.PlatformKeyInput;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.input.KeyEvent;

@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookComponent
{
    @Shadow
    EditBox searchBox;

    @Shadow
    String lastSearch;

    @SuppressWarnings("SameParameterValue")
    @Shadow
    abstract void updateCollections(boolean resetPageNumber, boolean filtering);

    @Shadow
    abstract boolean isFiltering();

    @Unique
    private static String lastSearchStatic = "";

    @Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.setValue(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void indicatia$initStaticLastSearch(CallbackInfo info)
    {
        if (Indicatia.CONFIG.saveLastSearchInRecipeBook)
        {
            this.searchBox.setValue(lastSearchStatic);
        }
    }

    @Inject(method = "checkSearchStringUpdate", at = @At(value = "FIELD", target = "net/minecraft/client/gui/screens/recipebook/RecipeBookComponent.lastSearch:Ljava/lang/String;", opcode = Opcodes.PUTFIELD))
    private void indicatia$staticLastSearchUpdate(CallbackInfo info, @Local String string)
    {
        if (Indicatia.CONFIG.saveLastSearchInRecipeBook)
        {
            lastSearchStatic = string;
        }
    }

    @ModifyExpressionValue(method = "mouseClicked", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.mouseClicked(DDI)Z", ordinal = 0))
    private boolean indicatia$rightClickClearText(boolean original, double mouseX, double mouseY, int button)
    {
        var inBox = mouseX >= (double) this.searchBox.getX() && mouseX < (double) (this.searchBox.getX() + this.searchBox.getWidth()) && mouseY >= (double) this.searchBox.getY() && mouseY < (double) (this.searchBox.getY() + this.searchBox.getHeight());

        if (Indicatia.CONFIG.saveLastSearchInRecipeBook && !this.searchBox.getValue().isEmpty() && inBox && button == 1)
        {
            this.searchBox.setValue("");
            this.lastSearch = "";
            lastSearchStatic = "";
            this.updateCollections(false, this.isFiltering());
        }
        return original;
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "net/minecraft/client/KeyMapping.matches(Lnet/minecraft/client/input/KeyEvent;)Z"))
    private boolean indicatia$addAltChatKey(boolean original, KeyEvent keyEvent)
    {
        return original || PlatformKeyInput.isAltChatMatches(keyEvent);
    }
}