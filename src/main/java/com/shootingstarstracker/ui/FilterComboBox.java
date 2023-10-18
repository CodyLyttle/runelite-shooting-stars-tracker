package com.shootingstarstracker.ui;

import net.runelite.client.ui.components.ComboBoxListRenderer;

import javax.swing.*;
import java.awt.*;

import static net.runelite.client.ui.PluginPanel.PANEL_WIDTH;

public class FilterComboBox<T> extends JPanel
{   
    private final JLabel label;
    private final JComboBox<T> comboBox;

    public FilterComboBox(String text, T[] items)
    {
        label = new FilterLabel(text);
        comboBox = new JComboBox<>(items);
        comboBox.setRenderer(new ComboBoxListRenderer<>());
        comboBox.setPreferredSize(new Dimension((int) (comboBox.getPreferredSize().getWidth()), 25));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFocusable(false);

        setLayout(new BorderLayout());
        add(label, BorderLayout.WEST);
        add(comboBox, BorderLayout.EAST);
    }

    public void setLabelTooltip(String text)
    {
        label.setToolTipText(text);
    }
}
