package com.shootingstarstracker.ui;


import lombok.Getter;

import javax.swing.event.ChangeEvent;

// A pair of sliders that share minimum/maximum values and are used to simulate a value range.
// When the minimum value exceeds the maximum value, the maximum value is updated to the minimum value and vice versa.
public class ConfigSliderRangePair
{
    @Getter
    private final int minValue;

    @Getter
    private final int maxValue;

    @Getter
    private final ConfigSlider configSliderMin;

    @Getter
    private final ConfigSlider configSliderMax;
    
    public ConfigSliderRangePair(int min, int max, int leftValue, int rightValue, String leftLabelPrefix, String rightLabelPrefix)
    {
        assert min <= max;
        assert leftValue >= min && leftValue <= max;
        assert rightValue >= min && rightValue <= max;
        assert leftValue <= rightValue;

        minValue = min;
        maxValue = max;
        configSliderMin = new ConfigSlider(min, max, leftValue, leftLabelPrefix);
        configSliderMax = new ConfigSlider(min, max, rightValue, rightLabelPrefix);

        configSliderMin.getSlider().addChangeListener(this::onMinSliderChanged);
        configSliderMax.getSlider().addChangeListener(this::onMaxSliderChanged);
    }

    private void onMinSliderChanged(ChangeEvent e)
    {
        int leftValue = configSliderMin.getSlider().getValue();
        int rightValue = configSliderMax.getSlider().getValue();
        if (leftValue > rightValue)
        {
            configSliderMax.getSlider().setValue(leftValue);
        }
    }

    private void onMaxSliderChanged(ChangeEvent e)
    {
        int leftValue = configSliderMin.getSlider().getValue();
        int rightValue = configSliderMax.getSlider().getValue();
        if (rightValue < leftValue)
        {
            configSliderMin.getSlider().setValue(rightValue);
        }
    }
    
    public void setLabelTooltips(String leftTooltip, String rightTooltip)
    {
        configSliderMin.setLabelTooltip(leftTooltip);
        configSliderMax.setLabelTooltip(rightTooltip);
    }

    public void setRange(int leftValue, int rightValue)
    {
        assert leftValue >= minValue && leftValue <= maxValue;
        assert rightValue >= minValue && rightValue <= maxValue;

        configSliderMin.setValue(leftValue);
        configSliderMax.setValue(rightValue);
    }
}
