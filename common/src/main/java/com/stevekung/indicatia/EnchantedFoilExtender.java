package com.stevekung.indicatia;

public interface EnchantedFoilExtender
{
    default void indicatia$setFoil(boolean hasFoil)
    {
        throw new AssertionError("Implemented via mixin");
    }

    default boolean indicatia$hasFoil()
    {
        throw new AssertionError("Implemented via mixin");
    }
}