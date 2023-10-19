package com.shootingstarstracker.ui;

public class StarFilterPanel extends CollapsablePanel
{
    private final FilterCheckBox checkBoxF2P;
    private final FilterCheckBox checkBoxWilderness;
    private final FilterCheckBox checkBoxPVP;
    private final FilterComboBox<Integer> comboTotalLevel;
    private final FilterSlider sliderStarTierMin;
    private final FilterSlider sliderStarTierMax;
    private final FilterSlider sliderMaxStarAge;
    private Runnable filterChangedCallback;

    public StarFilterPanel()
    {
        super("Filter");

        checkBoxF2P = new FilterCheckBox("Hide F2P");
        checkBoxF2P.setLabelTooltip("Hide stars in free to play worlds");
        checkBoxF2P.subscribe(this::onFilterChanged);
        
        checkBoxPVP = new FilterCheckBox("Hide PVP");
        checkBoxPVP.setLabelTooltip("Hide stars in player-vs-player worlds");
        checkBoxPVP.subscribe(this::onFilterChanged);

        checkBoxWilderness = new FilterCheckBox("Hide Wilderness");
        checkBoxWilderness.setLabelTooltip("Hide stars landed in the wilderness");
        checkBoxWilderness.subscribe(this::onFilterChanged);

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

        addContentElement(checkBoxF2P);
        addContentElement(checkBoxPVP);
        addContentElement(checkBoxWilderness);
        addContentElement(comboTotalLevel);
        addContentElement(sliderStarTierMin);
        addContentElement(sliderStarTierMax);
        addContentElement(sliderMaxStarAge);
    }

    public boolean getHideF2PWorlds()
    {
        return checkBoxF2P.getValue();
    }
    
    public boolean getHidePVPWorlds()
    {
    return checkBoxPVP.getValue();
    }

    public boolean getHideWilderness()
    {
        return checkBoxWilderness.getValue();
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
        if (target == checkBoxF2P)
        {
            // TODO: Update config
        }
        else if(target == checkBoxPVP)
        {
            // TODO: Update config
        }
        else if (target == checkBoxWilderness)
        {
            // TODO: Update config
        }
        else if (target == comboTotalLevel)
        {
            // TODO: Update config
        }
        else if (target == sliderStarTierMin)
        {
            // TODO: Update config
        }
        else if (target == sliderStarTierMax)
        {
            // TODO: Update config
        }
        else if (target == sliderMaxStarAge)
        {
            // TODO: Update config
        }
        
        if(filterChangedCallback != null)
            filterChangedCallback.run();
    }
}
