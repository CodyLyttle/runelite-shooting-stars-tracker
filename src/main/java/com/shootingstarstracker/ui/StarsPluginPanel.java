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
        northPanel.add(createConfigPanel(), BorderLayout.CENTER);

        // Star info.
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(0, 1));

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
    }

    private JPanel createConfigPanel()
    {
        ConfigPanel configPanel = new ConfigPanel("Config");

        // Check boxes
        ConfigCheckBox cbF2P = new ConfigCheckBox("F2P stars");
        cbF2P.setLabelTooltip("Include stars in free to play worlds");
        ConfigCheckBox cbWilderness = new ConfigCheckBox("Wilderness stars");
        cbWilderness.setLabelTooltip("Include stars in the wilderness");

        // TODO: Replace with combo box: Max total level { 500,750,1250,1500,1750,2000,2200 }
        ConfigCheckBox cbTotalLevel = new ConfigCheckBox("Total level worlds");
        cbTotalLevel.setLabelTooltip("Include stars in total level worlds");

        configPanel.add(cbF2P);
        configPanel.add(cbWilderness);
        configPanel.add(cbTotalLevel);

        // Sliders
        ConfigSliderRangePair sliderRangeStarTier = new ConfigSliderRangePair(1, 9, 1, 9, "Min tier: ", "Max tier");
        sliderRangeStarTier.setLabelTooltips("Exclude stars below this tier", "Exclude stars above this tier");
        ConfigSlider sliderMaxMinutes = new ConfigSlider(1, 120, 60, "Minutes: ");
        sliderMaxMinutes.setLabelTooltip("Exclude stars that landed more than this many minutes ago");

        configPanel.add(sliderRangeStarTier.getConfigSliderMin());
        configPanel.add(sliderRangeStarTier.getConfigSliderMax());
        configPanel.add(sliderMaxMinutes);

        return configPanel;
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
