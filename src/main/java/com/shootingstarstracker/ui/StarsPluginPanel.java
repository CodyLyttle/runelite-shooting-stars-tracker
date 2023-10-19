package com.shootingstarstracker.ui;

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

@Slf4j
public class StarsPluginPanel extends PluginPanel
{
    private final static int PANEL_SPACER = 8;

    // Containers
    private final JPanel northPanel;
    private final JPanel southPanel;
    private final StarFilterPanel filterPanel;

    private List<StarRow> starRows = new ArrayList<>();
    private List<ShootingStar> stars = new ArrayList<>();

    private final ShootingStarsFetcher starsFetcher;

    private final WorldHopper worldHopper;

    public StarsPluginPanel(ShootingStarsFetcher starsFetcher, WorldHopper worldHopper)
    {
        this.starsFetcher = starsFetcher;
        this.worldHopper = worldHopper;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header and config.
        northPanel = new JPanel(new BorderLayout());
        filterPanel = new StarFilterPanel();
        filterPanel.setFilterChangedCallback(this::filterStars);
        northPanel.add(filterPanel, BorderLayout.CENTER);

        // Star info.
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(0, 1));

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
    }

    @Override
    public void onActivate()
    {
        // TODO: Use a placeholder panel until stars are fetched.
        LoadStars();
    }

    @Override
    public void onDeactivate()
    {
        stars.clear();
        starRows.clear();
        southPanel.removeAll();
        southPanel.invalidate();
    }

    public void LoadStars()
    {
        starRows.clear();
        stars = starsFetcher.fetchStars();

        boolean useAlternativeColor = false;
        for (ShootingStar star : stars)
        {
            try
            {
                StarRow row = new StarRow(star, useAlternativeColor, this::hopToStarWorld);
                starRows.add(row);
                useAlternativeColor = !useAlternativeColor;
            }
            catch (NullPointerException ex)
            {
                // Prevent error with bad star data until I figure out the issue.
                log.error("Star is null or has a null property");
            }
        }

        filterStars();
    }

    // TODO: Optimize.
    public void filterStars()
    {
        southPanel.removeAll();

        for (StarRow row : starRows)
        {
            ShootingStar star = row.getStar();

            boolean isFiltered = star.getTier() < filterPanel.getMinTier()
                    || star.getTier() > filterPanel.getMaxTier()
                    || star.getMinutesAgo() > filterPanel.getMaxStarAge()
                    || isFilteredWorld(star);

            if (!isFiltered)
                southPanel.add(row);
        }

        southPanel.invalidate();
        southPanel.repaint();
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
                if(level > maxLevel)
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
}
