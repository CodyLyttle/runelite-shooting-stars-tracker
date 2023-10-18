package com.shootingstarstracker.ui;

import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.services.ShootingStarsFetcher;
import com.shootingstarstracker.services.WorldHopper;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StarsPluginPanel extends PluginPanel
{
    private final static int PANEL_SPACER = 8;

    // Containers
    private final JPanel northPanel;
    private final JPanel southPanel;

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
        northPanel.add(createFilterPanel(), BorderLayout.CENTER);

        // Star info.
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(0, 1));

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
    }

    private JPanel createFilterPanel()
    {
        CollapsablePanel filterPanel = new CollapsablePanel("Filter");

        // Check boxes
        FilterCheckBox cbF2P = new FilterCheckBox("F2P stars");
        cbF2P.setLabelTooltip("Include stars in free to play worlds");
        FilterCheckBox cbWilderness = new FilterCheckBox("Wilderness stars");
        cbWilderness.setLabelTooltip("Include stars in the wilderness");

        FilterComboBox<Integer> comboTotalLevel = new FilterComboBox<>("Max total level", new Integer[]{0, 500, 1000, 1250, 1500, 1750, 2000, 2200});
        comboTotalLevel.setLabelTooltip("Include stars in total level worlds up to and including this value.");

        filterPanel.add(cbF2P);
        filterPanel.add(cbWilderness);
        filterPanel.add(comboTotalLevel);

        // Sliders
        FilterSliderRangePair sliderRangeStarTier = new FilterSliderRangePair(1, 9, 1, 9, "Min tier: ", "Max tier: ");
        sliderRangeStarTier.setLabelTooltips("Exclude stars below this tier", "Exclude stars above this tier");
        FilterSlider sliderMaxMinutes = new FilterSlider(1, 120, 60, "Minutes: ");
        sliderMaxMinutes.setLabelTooltip("Exclude stars that landed more than this many minutes ago");

        filterPanel.add(sliderRangeStarTier.getFilterSliderMin());
        filterPanel.add(sliderRangeStarTier.getFilterSliderMax());
        filterPanel.add(sliderMaxMinutes);

        return filterPanel;
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
        southPanel.removeAll();
        southPanel.invalidate();
    }

    public void Populate()
    {
        stars = starsFetcher.fetchStars();

        boolean useAlternativeColor = false;
        for (ShootingStar star : stars)
        {
            try
            {
                StarRow row = new StarRow(star, useAlternativeColor, this::hopToStarWorld);
                southPanel.add(row);
                starRows.add(row);
                useAlternativeColor = !useAlternativeColor;
            }
            catch (NullPointerException ex)
            {
                // Prevent error with bad star data until I figure out the issue.
                log.error("Star is null or has a null property");
            }
        }

        southPanel.revalidate();
        southPanel.repaint();
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
