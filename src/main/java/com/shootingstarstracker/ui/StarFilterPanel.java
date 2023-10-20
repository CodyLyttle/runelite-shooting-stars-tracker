package com.shootingstarstracker.ui;

import com.shootingstarstracker.services.FilterConfigManager;
import com.shootingstarstracker.enums.FilterKey;
import com.shootingstarstracker.ui.elements.*;

public class StarFilterPanel extends CollapsablePanel
{
    private final FilterCheckBox checkBoxHideF2P;
    private final FilterCheckBox checkBoxHidePVP;
    private final FilterCheckBox checkBoxHideWilderness;
    private final FilterComboBox<Integer> comboTotalLevel;
    private final FilterSlider sliderStarTierMin;
    private final FilterSlider sliderStarTierMax;
    private final FilterSlider sliderMaxStarAge;
    private final FilterConfigManager filterConfigManager;
    private Runnable filterChangedCallback;

    public StarFilterPanel(FilterConfigManager filterConfigManager)
    {
        super("Filter");
        this.filterConfigManager = filterConfigManager;

        checkBoxHideF2P = new FilterCheckBox("Hide F2P");
        checkBoxHideF2P.setLabelTooltip("Hide stars in free to play worlds");
        checkBoxHideF2P.subscribe(this::onFilterChanged);

        checkBoxHidePVP = new FilterCheckBox("Hide PVP");
        checkBoxHidePVP.setLabelTooltip("Hide stars in player-vs-player worlds");
        checkBoxHidePVP.subscribe(this::onFilterChanged);

        checkBoxHideWilderness = new FilterCheckBox("Hide Wilderness");
        checkBoxHideWilderness.setLabelTooltip("Hide stars landed in the wilderness");
        checkBoxHideWilderness.subscribe(this::onFilterChanged);

        comboTotalLevel = new FilterComboBox<>("Total level", new Integer[]{0, 500, 1000, 1250, 1500, 1750, 2000, 2200});
        comboTotalLevel.setLabelTooltip("Hide stars in total level worlds exceeding this value");
        comboTotalLevel.subscribe(this::onFilterChanged);

        FilterSliderRangePair sliderRangeStarTier = new FilterSliderRangePair(1, 9, 1, 9, "Min tier: ", "Max tier: ");
        sliderStarTierMin = sliderRangeStarTier.getFilterSliderMin();
        sliderStarTierMax = sliderRangeStarTier.getFilterSliderMax();
        sliderStarTierMin.setLabelTooltip("Hide stars below this tier");
        sliderStarTierMax.setLabelTooltip("Hide stars above this tier");
        sliderStarTierMin.subscribe(this::onFilterChanged);
        sliderStarTierMax.subscribe(this::onFilterChanged);

        sliderMaxStarAge = new FilterSlider(1, 120, 60, "Minutes: ");
        sliderMaxStarAge.setLabelTooltip("Hide stars that landed more than this many minutes ago");
        sliderMaxStarAge.subscribe(this::onFilterChanged);

        loadValuesFromConfig();

        addContentElement(checkBoxHideF2P);
        addContentElement(checkBoxHidePVP);
        addContentElement(checkBoxHideWilderness);
        addContentElement(comboTotalLevel);
        addContentElement(sliderStarTierMin);
        addContentElement(sliderStarTierMax);
        addContentElement(sliderMaxStarAge);
    }

    private void loadValuesFromConfig()
    {
        checkBoxHideF2P.setValue(Boolean.parseBoolean(filterConfigManager.load(FilterKey.HIDE_F2P)));
        checkBoxHidePVP.setValue(Boolean.parseBoolean(filterConfigManager.load(FilterKey.HIDE_PVP)));
        checkBoxHideWilderness.setValue(Boolean.parseBoolean(filterConfigManager.load(FilterKey.HIDE_WILDERNESS)));
        comboTotalLevel.setValue(Integer.parseInt(filterConfigManager.load(FilterKey.TOTAL_LEVEL)));
        sliderStarTierMin.setValue(Integer.parseInt(filterConfigManager.load(FilterKey.MIN_TIER)));
        sliderStarTierMax.setValue(Integer.parseInt(filterConfigManager.load(FilterKey.MAX_TIER)));
        sliderMaxStarAge.setValue(Integer.parseInt(filterConfigManager.load(FilterKey.MAX_STAR_AGE)));
    }

    public boolean getHideF2PWorlds()
    {
        return checkBoxHideF2P.getValue();
    }

    public boolean getHidePVPWorlds()
    {
        return checkBoxHidePVP.getValue();
    }

    public boolean getHideWilderness()
    {
        return checkBoxHideWilderness.getValue();
    }

    public Integer getMaxTotalLevel()
    {
        return comboTotalLevel.getValue();
    }

    public Integer getMinTier()
    {
        return sliderStarTierMin.getValue();
    }

    public Integer getMaxTier()
    {
        return sliderStarTierMax.getValue();
    }

    public Integer getMaxStarAge()
    {
        return sliderMaxStarAge.getValue();
    }

    public void setFilterChangedCallback(Runnable callback)
    {
        filterChangedCallback = callback;
    }

    private void onFilterChanged(Object target, Object value)
    {
        if (target == checkBoxHideF2P)
        {
            filterConfigManager.save(FilterKey.HIDE_F2P, value);
        }
        else if (target == checkBoxHidePVP)
        {
            filterConfigManager.save(FilterKey.HIDE_PVP, value);
        }
        else if (target == checkBoxHideWilderness)
        {
            filterConfigManager.save(FilterKey.HIDE_WILDERNESS, value);
        }
        else if (target == comboTotalLevel)
        {
            filterConfigManager.save(FilterKey.TOTAL_LEVEL, value);
        }
        else if (target == sliderStarTierMin)
        {
            filterConfigManager.save(FilterKey.MIN_TIER, value);
        }
        else if (target == sliderStarTierMax)
        {
            filterConfigManager.save(FilterKey.MAX_TIER, value);
        }
        else if (target == sliderMaxStarAge)
        {
            filterConfigManager.save(FilterKey.MAX_STAR_AGE, value);
        }

        if (filterChangedCallback != null)
            filterChangedCallback.run();
    }
}
