package com.shootingstarstracker.ui;

import com.shootingstarstracker.models.ShootingStar;
import lombok.Getter;
import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StarRow extends JPanel
{
    private final static int LEFT_COL_WIDTH = 54;
    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    private final static Color BACKGROUND_COLOR_ALTERNATIVE = ColorScheme.DARKER_GRAY_COLOR;
    private final static Color HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;
    private final static Color PRESSED_COLOR = ColorScheme.MEDIUM_GRAY_COLOR;

    @Getter
    @Setter
    private boolean useAlternativeColors;

    StarRow(ShootingStar star, boolean useAlternativeColors)
    {
        this.useAlternativeColors = useAlternativeColors;
        
        // Label strings.
        String timeText = star.getTime() + "m ago";
        String tierText = "Tier: " + star.getTier();
        String regionAndWorldText = star.getRegion() + " - W" + star.getWorld();
        String locationText = star.getLoc();

        JLabel timeLabel = createLabel(timeText);
        JLabel tierLabel = createLabel(tierText);
        JLabel regionAndWorldLabel = createLabel(regionAndWorldText);
        JLabel locationLabel = createLabel(locationText);
        locationLabel.setToolTipText(locationText);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(1, 0, 1, 0));
        setBackground(getBackgroundColor());

        JPanel leftColumn = createColumn();
        leftColumn.setPreferredSize(new Dimension(LEFT_COL_WIDTH, 0));
        leftColumn.add(timeLabel);
        leftColumn.add(tierLabel);

        JPanel rightColumn = createColumn();
        rightColumn.add(regionAndWorldLabel);
        rightColumn.add(locationLabel);

        add(leftColumn, BorderLayout.WEST);
        add(rightColumn, BorderLayout.CENTER);

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // TODO: Hop worlds.
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                setBackground(HOVER_COLOR);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setBackground(getBackgroundColor());
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        // Forward events to underlying container.
        // By default, controls containing tooltips consume the event.
        JPanel instanceContainer = this;
        locationLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, instanceContainer));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, instanceContainer));
            }
        });
    }

    private JPanel createColumn()
    {
        JPanel column = new JPanel();
        column.setBorder(new EmptyBorder(6, 6, 6, 6));
        column.setLayout(new GridLayout(2, 1, 0, 6));
        column.setOpaque(false);

        return column;
    }

    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        label.setFont(FontManager.getRunescapeSmallFont());

        return label;
    }

    private Color getBackgroundColor()
    {
        return useAlternativeColors ? BACKGROUND_COLOR_ALTERNATIVE : BACKGROUND_COLOR;
    }
}