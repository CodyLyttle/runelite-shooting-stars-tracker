package com.shootingstarstracker.ui;

import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.services.ShootingStarsFetcher;
import com.shootingstarstracker.services.WorldHopper;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StarsPluginPanel extends PluginPanel
{
    private final JPanel listContainer = new JPanel();
    private List<StarRow> starRows = new ArrayList<>();
    private List<ShootingStar> stars = new ArrayList<>();

    private final ShootingStarsFetcher starsFetcher;
    
    private final WorldHopper worldHopper;

    public StarsPluginPanel(ShootingStarsFetcher starsFetcher, WorldHopper worldHopper)
    {
        this.starsFetcher = starsFetcher;
        this.worldHopper = worldHopper;
        
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
        stars = starsFetcher.fetchStars();
        
        boolean useAlternativeColor = false;
        for(ShootingStar star : stars)
        {
            StarRow row = new StarRow(star, useAlternativeColor, this::hopToStarWorld);
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
    
    private void hopToStarWorld(ShootingStar star)
    {
        worldHopper.hop(star.getWorld());
    }
}
