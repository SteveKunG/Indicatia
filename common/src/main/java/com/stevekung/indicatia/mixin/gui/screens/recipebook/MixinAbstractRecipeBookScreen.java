package com.stevekung.indicatia.mixin.gui.screens.recipebook;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.mixin.accessor.RecipeBookComponentAccessor;
import com.stevekung.indicatia.utils.PlatformKeyInput;

import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

@Mixin(AbstractRecipeBookScreen.class)
public class MixinAbstractRecipeBookScreen
{
    @Shadow
    @Final
    RecipeBookComponent<?> recipeBookComponent;

    @ModifyExpressionValue(method = "mouseClicked", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screens/recipebook/RecipeBookComponent.mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z", ordinal = 0))
    private boolean indicatia$rightClickClearText(boolean original, MouseButtonEvent mouseButtonEvent, boolean doubleClicked)
    {
        var searchBox = ((RecipeBookComponentAccessor) this.recipeBookComponent).indicatia$getSearchBox();
        var inBox = mouseButtonEvent.x() >= searchBox.getX() && mouseButtonEvent.x() < searchBox.getX() + searchBox.getWidth() && mouseButtonEvent.y() >= searchBox.getY() && mouseButtonEvent.y() < searchBox.getY() + searchBox.getHeight();

        if (Indicatia.CONFIG.saveLastSearchInRecipeBook && !searchBox.getValue().isEmpty() && inBox && mouseButtonEvent.isRightMouse())
        {
            searchBox.setValue("");
            ((RecipeBookComponentAccessor) this.recipeBookComponent).indicatia$setLastSearch("");
            Indicatia.lastSearchStatic = "";
            ((RecipeBookComponentAccessor) this.recipeBookComponent).callUpdateCollections(false, ((RecipeBookComponentAccessor) this.recipeBookComponent).callIsFiltering());
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