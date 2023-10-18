package com.shootingstarstracker.ui;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

// Imitates checkbox control used in config panel.
// TODO: Link to config property.
public class ConfigCheckBox extends JPanel
{

    @Getter
    private final JLabel label;

    @Getter
    private final JCheckBox checkBox;

    public ConfigCheckBox(String text)
    {
        label = new ConfigLabel(text);
        checkBox = new JCheckBox();
        checkBox.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
        checkBox.addChangeListener(this::onIsSelectedChanged);
        
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(checkBox, BorderLayout.EAST);
        syncWithConfig();
    }

    public boolean getIsSelected()
    {
        return checkBox.isSelected();
    }

    public void setIsSelected(boolean value)
    {
        checkBox.setSelected(value);
    }
    
    public void setLabelTooltip(String tooltip)
    {
        label.setToolTipText(tooltip);
    }
    
    private void syncWithConfig()
    {
        // TODO: Load value from config.
    }
    
    private void onIsSelectedChanged(ChangeEvent e)
    {
        // TODO: Set config value.
    }
}
