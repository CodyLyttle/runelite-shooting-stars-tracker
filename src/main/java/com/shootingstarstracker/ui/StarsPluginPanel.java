package com.shootingstarstracker.ui;

import com.shootingstarstracker.FilterConfigManager;
import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.services.ShootingStarsFetcher;
import com.shootingstarstracker.services.WorldHopper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.WorldType;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StarsPluginPanel extends PluginPanel
{
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> updateLoopFuture;

    // Containers
    private final StarFilterPanel filterPanel;
    private final JPanel starPanel;

    private List<StarRow> starRows = new ArrayList<>();
    private final ShootingStarsFetcher starsFetcher;
    private final WorldHopper worldHopper;


    public StarsPluginPanel(FilterConfigManager filterConfigManager, ShootingStarsFetcher starsFetcher, WorldHopper worldHopper)
    {
        this.starsFetcher = starsFetcher;
        this.worldHopper = worldHopper;
        setDoubleBuffered(true);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header and config.
        JPanel northPanel = new JPanel(new BorderLayout());
        filterPanel = new StarFilterPanel(filterConfigManager);
        filterPanel.setFilterChangedCallback(this::filterStars);
        northPanel.add(filterPanel, BorderLayout.CENTER);

        // Star info.
        starPanel = new JPanel();
        starPanel.setLayout(new GridLayout(0, 1));

        add(northPanel, BorderLayout.NORTH);
        add(starPanel, BorderLayout.CENTER);
    }

    @Override
    public void onActivate()
    {
        // TODO: Use a placeholder panel until stars are fetched.
        updateLoopFuture = scheduler.scheduleAtFixedRate(this::LoadStars, 0, 60, TimeUnit.SECONDS);
    }

    @Override
    public void onDeactivate()
    {
        reset();
    }

    private void LoadStars()
    {
        List<ShootingStar> stars = starsFetcher.fetchStars();

        SwingUtilities.invokeLater(() ->
        {
            List<StarRow> updatedRows = new ArrayList<>();
            boolean useAlternativeColor = false;

            for (ShootingStar star : stars)
            {
                try
                {
                    StarRow row = new StarRow(star, useAlternativeColor, this::hopToStarWorld);
                    updatedRows.add(row);
                    useAlternativeColor = !useAlternativeColor;
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

            for (StarRow row : starRows)
            {
                ShootingStar star = row.getStar();

                boolean isFiltered = star.getTier() < filterPanel.getMinTier()
                        || star.getTier() > filterPanel.getMaxTier()
                        || star.getMinutesAgo() > filterPanel.getMaxStarAge()
                        || isFilteredWorld(star);

                if (!isFiltered)
                    starPanel.add(row);
            }

            starPanel.revalidate();
            starPanel.repaint();
        });
    }

    private boolean isFilteredWorld(ShootingStar star)
    {
        EnumSet<WorldType> worldTypes = star.getWorld().getTypes();

        if (filterPanel.getHideF2PWorlds() && !worldTypes.contains(WorldType.MEMBERS))
            return true;

        if (filterPanel.getHidePVPWorlds() && WorldType.isPvpWorld(worldTypes))
            return true;

        if (worldTypes.contains(WorldType.SKILL_TOTAL))
        {
            try
            {
                String levelString = star.getWorld().getActivity().split(" ", 2)[0];
                int level = Integer.parseInt(levelString);
                int maxLevel = filterPanel.getMaxTotalLevel();
                if (level > maxLevel)
                    return true;
            }
            catch (NumberFormatException e)
            {
                log.error("Failed to parse total level integer from total level world");
            }
        }

        if (filterPanel.getHideWilderness() && star.getLocation().toLowerCase().contains("wilderness"))
            return true;

        return false;
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
