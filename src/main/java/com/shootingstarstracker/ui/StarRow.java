package com.shootingstarstracker.ui;

import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.Parser;
import lombok.Getter;
import net.runelite.api.WorldType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumSet;
import java.util.function.Consumer;

// TODO: Highlight current world.
public class StarRow extends JPanel
{
    private final static int LEFT_COL_WIDTH = 54;
    private final static Color BACKGROUND_COLOR = ColorScheme.DARK_GRAY_COLOR;
    private final static Color BACKGROUND_COLOR_ALTERNATIVE = ColorScheme.DARKER_GRAY_COLOR;
    private final static Color HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;

    private boolean useAlternativeColors;

    @Getter
    private final ShootingStar star;

    StarRow(ShootingStar star, Consumer<ShootingStar> onSelect)
    {
        this.star = star;

        // Label strings.
        String timeText = star.getMinutesAgo() + "m ago";
        String tierText = "Tier: " + star.getTier();
        String regionAndWorldText = star.getRegion() + " - W" + star.getWorld().getId();
        String locationText = star.getLocation();

        JLabel timeLabel = createLabel(timeText);
        JLabel tierLabel = createLabel(tierText);
        JLabel regionAndWorldLabel = createLabel(regionAndWorldText);
        regionAndWorldLabel.setToolTipText(createWorldTooltip());
        JLabel locationLabel = createLabel(locationText);
        locationLabel.setToolTipText(locationText);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 6, 0, 6));
        setBackground(getBackgroundColor());

        JPanel leftColumn = createColumn();
        leftColumn.setPreferredSize(new Dimension(LEFT_COL_WIDTH, leftColumn.getPreferredSize().height));
        leftColumn.add(timeLabel);
        leftColumn.add(tierLabel);

        JPanel rightColumn = createColumn();
        rightColumn.add(regionAndWorldLabel);
        rightColumn.add(locationLabel);

        add(leftColumn, BorderLayout.WEST);
        add(rightColumn, BorderLayout.CENTER);

        // Current instance is stored as a variable to use within events.
        StarRow starRowInstance = this;

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (onSelect == null)
                    return;

                onSelect.accept(star);
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
        locationLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
            }
        });

        // Forward events to underlying container.
        // By default, controls containing tooltips consume the event.
        regionAndWorldLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, starRowInstance));
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

    private String createWorldTooltip()
    {
        final String newLinePrefix = "<br>";

        final int number = star.getWorld().getId();
        final int population = star.getWorld().getPlayerCount();
        final String activity = star.getWorld().getActivity();
        final EnumSet<WorldType> types = star.getWorld().getTypes();

        StringBuilder builder = new StringBuilder();

        // Example tooltip:
        // 🌍123 👤2000
        // - Fishing Trawler
        // - Members

        builder.append("\uD83C\uDF0D").append(number).append(" \uD83D\uDC64").append(population);

        // Add world specific activity.
        if (!activity.equals("-"))
        {
            builder.append(newLinePrefix).append(activity);
        }

        // Account for missing F2P tag
        if (!types.contains(WorldType.MEMBERS))
        {
            builder.append(newLinePrefix).append("F2P");
        }

        for (WorldType type : types)
        {
            // Skill total is already displayed as an activity.
            if (type == WorldType.SKILL_TOTAL)
                continue;

            builder.append(newLinePrefix).append(Parser.enumStringToCamelCase(type.name()));
        }

        // Line breaks require html tags.
        return "<html>" + builder + "</html>";
    }

    public void useAlternativeColors(boolean value)
    {
        if (useAlternativeColors == value)
            return;

        useAlternativeColors = value;
        setBackground(getBackgroundColor());
    }
}
