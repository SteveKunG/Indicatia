package com.stevekung.indicatia.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

@Mixin(RecipeBookComponent.class)
public interface RecipeBookComponentAccessor
{
    @Accessor("searchBox")
    EditBox indicatia$getSearchBox();

    @Accessor("lastSearch")
    void indicatia$setLastSearch(String lastSearch);

    @Invoker
    void callUpdateCollections(boolean resetPage, boolean isFiltering);

    @Invoker
    boolean callIsFiltering();
}