package com.stevekung.indicatia;

public interface LivingEntityRenderStateExtender
{
    default void indicatia$setPlayerHead(boolean playerHead)
    {
        throw new AssertionError("Implemented via mixin");
    }

    default boolean indicatia$isPlayerHead()
    {
        throw new AssertionError("Implemented via mixin");
    }

    default void indicatia$setHeadGlint(boolean glint)
    {
        throw new AssertionError("Implemented via mixin");
    }

    default boolean indicatia$hasHeadGlint()
    {
        throw new AssertionError("Implemented via mixin");
    }
}