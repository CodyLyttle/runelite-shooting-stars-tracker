package com.shootingstarstracker;

import com.google.inject.Binder;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.shootingstarstracker.services.ShootingStarsFetcher;
import com.shootingstarstracker.services.WorldHopper;
import com.shootingstarstracker.ui.StarsPluginPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
        name = "Shooting Stars Tracker"
)
public class StarsPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private SpriteManager spriteManager;

    @Inject
    private StarsConfig config;
    
    @Inject
    private ShootingStarsFetcher starFetcher;
    
    @Inject
    private WorldHopper worldHopper;

    @Inject
    private ShootingStarsFetcher shootingStarsFetcher;
    
    private StarsPluginPanel sidePanel;

    private NavigationButton navButton;
    
    @Override
    public void configure(Binder binder)
    {
        super.configure(binder);
        binder.bind(ShootingStarsFetcher.class).in(Singleton.class);
        binder.bind(WorldHopper.class).in(Singleton.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        // Create side panel.
        sidePanel = new StarsPluginPanel(starFetcher, worldHopper);
        BufferedImage icon = ImageUtil.loadImageResource(StarsPlugin.class, "icon.png");
        navButton = NavigationButton.builder()
                .tooltip("Active Stars")
                .icon(icon)
                .priority(Integer.MAX_VALUE)
                .panel(sidePanel)
                .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception
    {
        // Remove side panel.
        clientToolbar.removeNavigation(navButton);
        sidePanel.reset();
        sidePanel = null;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        worldHopper.onGameTick();
    }

    @Provides
    StarsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(StarsConfig.class);
    }
}
