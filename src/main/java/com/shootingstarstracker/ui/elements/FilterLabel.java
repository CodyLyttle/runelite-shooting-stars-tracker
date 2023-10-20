package com.shootingstarstracker.ui.elements;

import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilterLabel extends JLabel
{
    private final static Color DEFAULT_COLOR = Color.WHITE;
    private final static Color HOVER_COLOR = ColorScheme.BRAND_ORANGE;
    
    public FilterLabel()
    {
        this("");
    }
    
    public FilterLabel(String text)
    {
        setForeground(DEFAULT_COLOR);
        setText(text);
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                setForeground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setForeground(DEFAULT_COLOR);
            }
        });
    }
}
