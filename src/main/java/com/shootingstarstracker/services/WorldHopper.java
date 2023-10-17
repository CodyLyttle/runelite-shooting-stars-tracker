package com.shootingstarstracker.services;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;

public class WorldHopper
{
    private final static int MAX_HOP_ATTEMPTS = 3;

    private int currentHopAttempts;

    @Inject
    Client client;

    @Getter
    private World pendingHop;

    public void hop(World world)
    {
        resetHopper();
        
        if (client.getGameState() == GameState.LOGIN_SCREEN)
        {
            client.changeWorld(world);
            return;
        }
        
        pendingHop = world;
    }

    public void onGameTick()
    {
        if (pendingHop == null)
            return;

        if (isWorldHopperOpen())
        {
            client.hopToWorld(pendingHop);
            resetHopper();
        }
        else if (currentHopAttempts == MAX_HOP_ATTEMPTS)
        {
            resetHopper();
        }
        else
        {
            client.openWorldHopper();
        }
    }

    private void resetHopper()
    {
        currentHopAttempts = 0;
        pendingHop = null;
    }

    private boolean isWorldHopperOpen()
    {
        return client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) != null;
    }
}
