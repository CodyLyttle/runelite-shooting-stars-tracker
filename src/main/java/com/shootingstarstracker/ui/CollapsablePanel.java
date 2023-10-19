package com.shootingstarstracker.ui;

import com.shootingstarstracker.StarsPlugin;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

// Expandable panel 
public class CollapsablePanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;
    private static final ImageIcon SECTION_EXPAND_ICON;
    private static final ImageIcon SECTION_EXPAND_ICON_HOVER;
    private static final ImageIcon SECTION_RETRACT_ICON;
    private static final ImageIcon SECTION_RETRACT_ICON_HOVER;
    
    private final JButton expandButton;
    
    private final JLabel headerLabel;
    
    private final JPanel contentPanel;
    
    private final JPanel northPanel;
    
    private final JPanel southPanel;

    @Getter
    private boolean isExpanded;

    static
    {
        // Copied directly from: https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/config/ConfigPanel.java
        BufferedImage sectionRetractIcon = ImageUtil.loadImageResource(StarsPlugin.class, "arrow_right.png");
        sectionRetractIcon = ImageUtil.luminanceOffset(sectionRetractIcon, -121);
        SECTION_EXPAND_ICON = new ImageIcon(sectionRetractIcon);
        SECTION_EXPAND_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(sectionRetractIcon, -100));
        final BufferedImage sectionExpandIcon = ImageUtil.rotateImage(sectionRetractIcon, Math.PI / 2);
        SECTION_RETRACT_ICON = new ImageIcon(sectionExpandIcon);
        SECTION_RETRACT_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(sectionExpandIcon, -100));
    }

    public CollapsablePanel(String headerText)
    {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(8,8,8,8));
        headerPanel.setBackground(BACKGROUND_COLOR);

        expandButton = createExpandButton();
        headerLabel = createHeaderLabel(headerText);
        headerPanel.add(expandButton, BorderLayout.WEST);
        headerPanel.add(createHeaderLabel(headerText), BorderLayout.CENTER);
        
        // Content panel
        contentPanel = new JPanel(new DynamicGridLayout(0, 1, 0, 5));
        contentPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        northPanel = wrapPanelWithBorder(headerPanel);
        southPanel = wrapPanelWithBorder(contentPanel);
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.CENTER);
        
        updateLayout();
    }
    
    private JPanel wrapPanelWithBorder(JPanel panel)
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        Border southBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.MEDIUM_GRAY_COLOR);
        wrapper.setBorder(southBorder);
        wrapper.add(panel, BorderLayout.CENTER);
        
        return wrapper;
    }

    private JButton createExpandButton()
    {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(18, 0));
        button.setBorder(new EmptyBorder(0, 0, 0, 5));
        SwingUtil.removeButtonDecorations(button);
        button.addActionListener(this::onButtonClick);
        return button;
    }

    private JLabel createHeaderLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(ColorScheme.BRAND_ORANGE);
        label.setFont(FontManager.getRunescapeBoldFont());
        
        return label;
    }
    

    private void onButtonClick(ActionEvent e)
    {
        if (e.getSource() != expandButton)
            return;

        isExpanded = !isExpanded;
        updateLayout();
    }

    public void updateLayout()
    {
        southPanel.setVisible(isExpanded);
        expandButton.setIcon(isExpanded ? SECTION_RETRACT_ICON : SECTION_EXPAND_ICON);
        expandButton.setRolloverIcon(isExpanded ? SECTION_RETRACT_ICON_HOVER : SECTION_EXPAND_ICON_HOVER);
        expandButton.setToolTipText(isExpanded ? "Retract" : "Expand");
    }

    public void addContentElement(JComponent component)
    {
        component.setBackground(BACKGROUND_COLOR);
        contentPanel.add(component);
    }
}
