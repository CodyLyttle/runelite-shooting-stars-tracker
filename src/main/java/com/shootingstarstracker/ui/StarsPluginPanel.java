package com.shootingstarstracker.ui;

import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.services.StarFetcher;
import com.shootingstarstracker.services.StarFilter;
import com.shootingstarstracker.services.WorldHopper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StarsPluginPanel extends PluginPanel
{
    private final FilterPanel filterPanel;
    private final JPanel starPanel;
    private final StarFetcher starFetcher;
    private final StarFilter starFilter;
    private final WorldHopper worldHopper;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> updateLoopFuture;
    private List<StarRow> starRows = new ArrayList<>();

    @Inject
    public StarsPluginPanel(StarFetcher fetcher, StarFilter filter, WorldHopper hopper)
    {
        this.starFetcher = fetcher;
        this.starFilter = filter;
        this.worldHopper = hopper;

        filterPanel = new FilterPanel(starFilter);
        starPanel = new JPanel(new GridLayout(0, 1));

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        add(filterPanel, BorderLayout.NORTH);
        add(starPanel, BorderLayout.CENTER);
    }

    @Override
    public void onActivate()
    {
        // TODO: Use a placeholder panel until stars are fetched.
        filterPanel.setFilterChangedCallback(this::filterStars);
        updateLoopFuture = scheduler.scheduleAtFixedRate(this::LoadStars, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    public void onDeactivate()
    {
        reset();
    }

    private void LoadStars()
    {
        List<ShootingStar> stars = starFetcher.fetchStars();

        SwingUtilities.invokeLater(() ->
        {
            List<StarRow> updatedRows = new ArrayList<>();

            for (ShootingStar star : stars)
            {
                try
                {
                    StarRow row = new StarRow(star, this::hopToStarWorld);
                    updatedRows.add(row);
                }
                catch (NullPointerException ex)
                {
                    // Prevent error with bad star data until I figure out the issue.
                    log.error("Star is null or has a null property");
                }
            }
            starRows = updatedRows;
        });

        filterStars();
    }

    // TODO: Optimize.
    private void filterStars()
    {
        SwingUtilities.invokeLater(() ->
        {
            starPanel.removeAll();
            boolean useAlternativeColor = false;

            for (StarRow row : starRows)
            {
                if (starFilter.meetsCriteria(row.getStar()))
                {
                    row.useAlternativeColors(useAlternativeColor);
                    useAlternativeColor = !useAlternativeColor;
                    starPanel.add(row);
                }
            }

            starPanel.revalidate();
            starPanel.repaint();
        });
    }

    private void hopToStarWorld(ShootingStar star)
    {
        worldHopper.hop(star.getWorld());
    }

    public void reset()
    {
        if (updateLoopFuture != null)
        {
            updateLoopFuture.cancel(true);
            updateLoopFuture = null;
        }

        filterPanel.setFilterChangedCallback(null);
        starRows.clear();
        starPanel.removeAll();
        starPanel.invalidate();
    }
}
