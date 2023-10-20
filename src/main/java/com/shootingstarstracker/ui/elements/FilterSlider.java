package com.shootingstarstracker.ui.elements;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.function.BiConsumer;

import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class FilterSlider extends JPanel implements IFilterElement<Integer>
{
    @Getter
    private final JSlider slider;

    @Getter
    private final JLabel label;

    private final String labelPrefix;

    public FilterSlider(int min, int max, int value, String labelPrefix)
    {
        assert min <= max;
        assert value >= min && value <= max;

        this.labelPrefix = labelPrefix;

        label = new FilterLabel();
        slider = new JSlider(min, max, value);
        slider.setBackground(Color.white);
        slider.addChangeListener(this::onSliderChanged);
        slider.setPreferredSize(new Dimension((int) (PANEL_WIDTH * 0.45), 25));

        setLayout(new BorderLayout());
        add(label, BorderLayout.WEST);
        add(slider, BorderLayout.EAST);

        updateLabel();
    }

    private void onSliderChanged(ChangeEvent e)
    {
        if (e.getSource() != slider)
            return;

        updateLabel();
    }

    @Override
    public Integer getValue()
    {
        return slider.getValue();
    }

    @Override
    public void setValue(Integer value)
    {
        assert value >= slider.getMinimum() && value <= slider.getMaximum();
        slider.setValue(value);
    }

    public void setLabelTooltip(String tooltip)
    {
        label.setToolTipText(tooltip);
    }

    @Override
    public void subscribe(BiConsumer<Object, Object> callback)
    {
        slider.addChangeListener((e) -> callback.accept(this, slider.getValue()));
    }

    private void updateLabel()
    {
        label.setText(labelPrefix + slider.getValue());
    }
}
