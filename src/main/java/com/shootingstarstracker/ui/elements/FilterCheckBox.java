package com.shootingstarstracker.ui.elements;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

// Imitates checkbox control used in config panel.
public class FilterCheckBox extends JPanel implements IFilterElement<Boolean>
{

    @Getter
    private final JLabel label;

    @Getter
    private final JCheckBox checkBox;

    public FilterCheckBox(String text)
    {
        label = new FilterLabel(text);
        checkBox = new JCheckBox();
        checkBox.setBackground(ColorScheme.LIGHT_GRAY_COLOR);

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(checkBox, BorderLayout.EAST);
    }

    public Boolean getValue()
    {
        return checkBox.isSelected();
    }

    public void setValue(Boolean value)
    {
        checkBox.setSelected(value);
    }

    public void setLabelTooltip(String tooltip)
    {
        label.setToolTipText(tooltip);
    }

    public void subscribe(BiConsumer<Object, Object> callback)
    {
        checkBox.addChangeListener((e) -> callback.accept(this, checkBox.isSelected()));
    }
}
