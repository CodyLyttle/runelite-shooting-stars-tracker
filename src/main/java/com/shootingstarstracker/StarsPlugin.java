package com.shootingstarstracker;

import com.google.inject.Binder;
import com.google.inject.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

import com.shootingstarstracker.services.ActiveStarsFetcher;
import com.shootingstarstracker.ui.StarsPluginPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
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
    private ActiveStarsFetcher activeStarsFetcher;

    private NavigationButton navButton;
    private StarsPluginPanel panel;
    

    @Override
    public void configure(Binder binder)
    {
        super.configure(binder);
        binder.bind(ActiveStarsFetcher.class).in(Singleton.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        BufferedImage icon = null;
        try
        {

            icon = ImageUtil.loadImageResource(StarsPlugin.class, "icon.png");
        }catch(Exception e)
        {
            log.error(e.getMessage());
        }
        panel = new StarsPluginPanel(this, activeStarsFetcher);
        navButton = NavigationButton.builder()
                .tooltip("Active Stars")
                .icon(icon)
                .priority(Integer.MAX_VALUE)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navButton);
        panel.Dispose();
        panel = null;
    }

    @Provides
    StarsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(StarsConfig.class);
    }
}
