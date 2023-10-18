package com.shootingstarstracker.ui;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class ConfigSlider extends JPanel
{
    @Getter
    private final JSlider slider;

    @Getter
    private final JLabel label;

    private final String labelPrefix;

    public ConfigSlider(int min, int max, int value, String labelPrefix)
    {
        assert min <= max;
        assert value >= min && value <= max;

        this.labelPrefix = labelPrefix;
        
        label = new ConfigLabel();
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

    public void setLabelTooltip(String tooltip)
    {
        label.setToolTipText(tooltip);
    }

    private void updateLabel()
    {
        label.setText(labelPrefix + slider.getValue());
    }

    public void setValue(int value)
    {
        assert value >= slider.getMinimum() && value <= slider.getMaximum();
        slider.setValue(value);
    }
}
