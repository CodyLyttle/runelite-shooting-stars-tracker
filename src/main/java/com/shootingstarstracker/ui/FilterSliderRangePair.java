package com.shootingstarstracker.ui;


import lombok.Getter;

import javax.swing.event.ChangeEvent;

// A pair of sliders that share minimum/maximum values and are used to simulate a value range.
// When the minimum value exceeds the maximum value, the maximum value is updated to the minimum value and vice versa.
public class FilterSliderRangePair
{
    @Getter
    private final int minValue;

    @Getter
    private final int maxValue;

    @Getter
    private final FilterSlider filterSliderMin;

    @Getter
    private final FilterSlider filterSliderMax;
    
    public FilterSliderRangePair(int min, int max, int leftValue, int rightValue, String leftLabelPrefix, String rightLabelPrefix)
    {
        assert min <= max;
        assert leftValue >= min && leftValue <= max;
        assert rightValue >= min && rightValue <= max;
        assert leftValue <= rightValue;

        minValue = min;
        maxValue = max;
        filterSliderMin = new FilterSlider(min, max, leftValue, leftLabelPrefix);
        filterSliderMax = new FilterSlider(min, max, rightValue, rightLabelPrefix);

        filterSliderMin.getSlider().addChangeListener(this::onMinSliderChanged);
        filterSliderMax.getSlider().addChangeListener(this::onMaxSliderChanged);
    }

    private void onMinSliderChanged(ChangeEvent e)
    {
        int leftValue = filterSliderMin.getSlider().getValue();
        int rightValue = filterSliderMax.getSlider().getValue();
        if (leftValue > rightValue)
        {
            filterSliderMax.getSlider().setValue(leftValue);
        }
    }

    private void onMaxSliderChanged(ChangeEvent e)
    {
        int leftValue = filterSliderMin.getSlider().getValue();
        int rightValue = filterSliderMax.getSlider().getValue();
        if (rightValue < leftValue)
        {
            filterSliderMin.getSlider().setValue(rightValue);
        }
    }
}
