package com.stevekung.indicatia.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

import com.mojang.logging.LogUtils;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.player.AbstractClientPlayer;

import org.slf4j.Logger;

public class FlashbackHelper
{
    private static final Logger LOGGER = LogUtils.getLogger();

    private static MethodHandle GET_CURRENT;
    private static MethodHandle GET_SPECTATING_PLAYER;
    private static VarHandle REPLAY_VISUALS;
    private static VarHandle SHOW_HOTBAR;
    private static VarHandle EXPORT_JOB;

    static
    {
        if (Indicatia.isFlashbackLoaded)
        {
            try
            {
                var lookup = MethodHandles.publicLookup();

                var editorStateManagerClazz = Class.forName("com.moulberry.flashback.state.EditorStateManager");
                var editorStateClazz = Class.forName("com.moulberry.flashback.state.EditorState");
                var replayVisualsClazz = Class.forName("com.moulberry.flashback.visuals.ReplayVisuals");
                var flashbackClazz = Class.forName("com.moulberry.flashback.Flashback");
                var exportJobClazz = Class.forName("com.moulberry.flashback.exporting.ExportJob");

                // static method EditorStateManager.getCurrent()
                GET_CURRENT = lookup.findStatic(editorStateManagerClazz, "getCurrent", MethodType.methodType(editorStateClazz));

                // instance field EditorState.replayVisuals
                REPLAY_VISUALS = lookup.findVarHandle(editorStateClazz, "replayVisuals", replayVisualsClazz);

                // instance field ReplayVisuals.showHotbar
                SHOW_HOTBAR = lookup.findVarHandle(replayVisualsClazz, "showHotbar", boolean.class);

                // static field Flashback.EXPORT_JOB
                EXPORT_JOB = lookup.findStaticVarHandle(flashbackClazz, "EXPORT_JOB", exportJobClazz);

                // static method Flashback.getSpectatingPlayer()
                GET_SPECTATING_PLAYER = lookup.findStatic(flashbackClazz, "getSpectatingPlayer", MethodType.methodType(AbstractClientPlayer.class));
            }
            catch (Throwable e)
            {
                LOGGER.error("Couldn't initialize Flashback compatibility", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isShowHotbar()
    {
        if (!Indicatia.isFlashbackLoaded)
        {
            return false;
        }

        try
        {
            if (GET_SPECTATING_PLAYER.invoke() == null)
            {
                return false;
            }

            var editorState = GET_CURRENT.invoke();

            if (editorState == null)
            {
                return false;
            }

            var replayVisuals = REPLAY_VISUALS.get(editorState);

            if (replayVisuals == null)
            {
                return false;
            }
            return (boolean) SHOW_HOTBAR.get(replayVisuals);
        }
        catch (Throwable e)
        {
            LOGGER.error("Failed to read showHotbar from Flashback", e);
            return false;
        }
    }

    public static boolean isExportJob()
    {
        if (!Indicatia.isFlashbackLoaded)
        {
            return false;
        }
        return EXPORT_JOB.get() != null;
    }
}