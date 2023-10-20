package com.shootingstarstracker.ui.elements;

import net.runelite.client.ui.components.ComboBoxListRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class FilterComboBox<T> extends JPanel implements IFilterElement<T>
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

    @Override
    public T getValue()
    {
        return comboBox.getItemAt(comboBox.getSelectedIndex());
    }

    @Override
    public void setValue(T value)
    {
        comboBox.setSelectedItem(value);
    }

    public void setLabelTooltip(String text)
    {
        label.setToolTipText(text);
    }

    @Override
    public void subscribe(BiConsumer<Object, Object> callback)
    {
        comboBox.addActionListener((e) -> callback.accept(this, comboBox.getItemAt(comboBox.getSelectedIndex())));
    }
}
