package com.stevekung.indicatia.mixin.gui.screens.recipebook;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

@Mixin(RecipeBookComponent.class)
public class MixinRecipeBookComponent
{
    @Shadow
    EditBox searchBox;

    @Inject(method = "initVisuals", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/EditBox.setValue(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void indicatia$initStaticLastSearch(CallbackInfo info)
    {
        if (Indicatia.CONFIG.saveLastSearchInRecipeBook)
        {
            this.searchBox.setValue(Indicatia.lastSearchStatic);
        }
    }

    @Inject(method = "checkSearchStringUpdate", at = @At(value = "FIELD", target = "net/minecraft/client/gui/screens/recipebook/RecipeBookComponent.lastSearch:Ljava/lang/String;", opcode = Opcodes.PUTFIELD))
    private void indicatia$staticLastSearchUpdate(CallbackInfo info, @Local(name = "searchText") String searchText)
    {
        if (Indicatia.CONFIG.saveLastSearchInRecipeBook)
        {
            Indicatia.lastSearchStatic = searchText;
        }
    }
}