package com.shootingstarstracker;

import com.google.inject.Binder;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.shootingstarstracker.enums.FilterKey;
import com.shootingstarstracker.services.FilterConfigManager;
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
    private final static boolean DEFAULT_FILTER_HIDE_F2P = false;
    private final static boolean DEFAULT_FILTER_HIDE_PVP = true;
    private final static boolean DEFAULT_FILTER_HIDE_WILDERNESS = true;
    private final static int DEFAULT_FILTER_TOTAL_LEVEL = 1750;
    private final static int DEFAULT_FILTER_MIN_TIER = 3;
    private final static int DEFAULT_FILTER_MAX_TIER = 6;
    private final static int DEFAULT_FILTER_MAX_STAR_AGE = 35;

    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ConfigManager configManager;

    @Inject
    private FilterConfigManager filterConfigManager;

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
        binder.bind(FilterConfigManager.class).toInstance(new FilterConfigManager("Shooting Stars Tracker"));
        binder.bind(ShootingStarsFetcher.class).in(Singleton.class);
        binder.bind(WorldHopper.class).in(Singleton.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        // Initialize any missing config values.
        filterConfigManager.initializeDefaultValue(FilterKey.HIDE_F2P, DEFAULT_FILTER_HIDE_F2P);
        filterConfigManager.initializeDefaultValue(FilterKey.HIDE_PVP, DEFAULT_FILTER_HIDE_PVP);
        filterConfigManager.initializeDefaultValue(FilterKey.HIDE_WILDERNESS, DEFAULT_FILTER_HIDE_WILDERNESS);
        filterConfigManager.initializeDefaultValue(FilterKey.TOTAL_LEVEL, DEFAULT_FILTER_TOTAL_LEVEL);
        filterConfigManager.initializeDefaultValue(FilterKey.MIN_TIER, DEFAULT_FILTER_MIN_TIER);
        filterConfigManager.initializeDefaultValue(FilterKey.MAX_TIER, DEFAULT_FILTER_MAX_TIER);
        filterConfigManager.initializeDefaultValue(FilterKey.MAX_STAR_AGE, DEFAULT_FILTER_MAX_STAR_AGE);

        // Create side panel.
        sidePanel = new StarsPluginPanel(filterConfigManager, starFetcher, worldHopper);
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
