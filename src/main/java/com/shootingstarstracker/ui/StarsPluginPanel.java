package com.shootingstarstracker.ui;

import com.shootingstarstracker.StarsPlugin;
import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.services.ActiveStarsFetcher;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StarsPluginPanel extends PluginPanel
{
    private static final int TIER_COL_WIDTH= 40;
    private static final int TIME_COL_WIDTH = 40;
    private static final int WORLD_COL_WIDTH = 40;
    
    private final StarsPlugin plugin;
    private final JPanel listContainer = new JPanel();
    private List<StarRow> starRows = new ArrayList<>();
    private List<ShootingStar> stars = new ArrayList<>();

    
    private final ActiveStarsFetcher starsFetcher;

    public StarsPluginPanel(StarsPlugin plugin, ActiveStarsFetcher starsFetcher)
    {
        this.plugin = plugin;
        this.starsFetcher = starsFetcher;
        
        setBorder(null);
        listContainer.setLayout(new GridLayout(0, 1));
        add(listContainer);
    }
    
    @Override
    public void onActivate()
    {
        // TODO: Use a placeholder panel until stars are fetched.
        Populate();
    }

    @Override
    public void onDeactivate()
    {
        stars.clear();
        starRows.clear();
        listContainer.removeAll();
        listContainer.invalidate();
    }
    
    public void Populate()
    {
        // Order by time, ascending.
        stars = starsFetcher.fetchShootingStars().stream()
                .sorted(Comparator.comparingInt(ShootingStar::getTime))
                .collect(Collectors.toList());
        
        boolean useAlternativeColor = false;
        for(ShootingStar star : stars)
        {
            StarRow row = new StarRow(star, useAlternativeColor);
            listContainer.add(row);
            starRows.add(row);
            useAlternativeColor = !useAlternativeColor;
        }
        
        listContainer.revalidate();
        listContainer.repaint();
    }
    
    public void Dispose()
    {
        // TODO: Cleanup
    }
}
