package com.shootingstarstracker.ui;

import com.shootingstarstracker.StarsPlugin;
import com.shootingstarstracker.services.StarFilter;
import com.shootingstarstracker.ui.elements.*;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

public class FilterPanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;
    private static final ImageIcon SECTION_EXPAND_ICON;
    private static final ImageIcon SECTION_EXPAND_ICON_HOVER;
    private static final ImageIcon SECTION_RETRACT_ICON;
    private static final ImageIcon SECTION_RETRACT_ICON_HOVER;

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

    private final JButton expandButton;
    private final JPanel contentPanel;
    private final JPanel wrappedContentPanel;

    private final FilterCheckBox checkBoxHideF2P;
    private final FilterCheckBox checkBoxHidePVP;
    private final FilterCheckBox checkBoxHideWilderness;
    private final FilterComboBox<Integer> comboTotalLevel;
    private final FilterSlider sliderStarTierMin;
    private final FilterSlider sliderStarTierMax;
    private final FilterSlider sliderMaxStarAge;
    private final StarFilter starFilter;

    private boolean isExpanded;
    private Runnable filterChangedCallback;

    public FilterPanel(StarFilter filter)
    {
        // Create header panel.
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        headerPanel.setBackground(BACKGROUND_COLOR);

        expandButton = createExpandButton();
        headerPanel.add(expandButton, BorderLayout.WEST);
        headerPanel.add(createFilterLabel(), BorderLayout.CENTER);
        headerPanel.add(createPortalLink(), BorderLayout.EAST);


        // Create content panel.
        contentPanel = new JPanel(new DynamicGridLayout(0, 1, 0, 5));
        contentPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        contentPanel.setBackground(BACKGROUND_COLOR);

        this.starFilter = filter;
        checkBoxHideF2P = createHideF2PCheckBox();
        checkBoxHidePVP = createHidePVPCheckBox();
        checkBoxHideWilderness = createHideWildernessCheckbox();
        comboTotalLevel = createTotalLevelComboBox();
        FilterSliderRangePair tierSliders = createStarTierSliders();
        sliderStarTierMin = tierSliders.getFilterSliderMin();
        sliderStarTierMax = tierSliders.getFilterSliderMax();
        sliderMaxStarAge = createMaxStarAgeSlider();

        addContentElement(checkBoxHideF2P);
        addContentElement(checkBoxHidePVP);
        addContentElement(checkBoxHideWilderness);
        addContentElement(comboTotalLevel);
        addContentElement(sliderStarTierMin);
        addContentElement(sliderStarTierMax);
        addContentElement(sliderMaxStarAge);

        // Add a southern grey border to both panels.
        JPanel wrappedHeaderPanel = wrapPanelWithBorder(headerPanel);
        wrappedContentPanel = wrapPanelWithBorder(contentPanel);


        setLayout(new BorderLayout());
        add(wrappedHeaderPanel, BorderLayout.NORTH);
        add(wrappedContentPanel, BorderLayout.SOUTH);

        updateLayout();
    }

    private JButton createExpandButton()
    {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(18, 0));
        button.setBorder(new EmptyBorder(0, 0, 0, 5));
        SwingUtil.removeButtonDecorations(button);
        button.addActionListener((e) ->
        {
            isExpanded = !isExpanded;
            updateLayout();
        });

        return button;
    }

    private JLabel createFilterLabel()
    {
        JLabel label = new JLabel("Filter");
        label.setForeground(ColorScheme.BRAND_ORANGE);
        label.setFont(FontManager.getRunescapeBoldFont());

        return label;
    }

    private JLabel createPortalLink()
    {
        final String text = "OSRS Portal";
        final String address = "https://osrsportal.com/shooting-stars-tracker";

        return new HyperlinkLabel(text, address);
    }

    private JPanel wrapPanelWithBorder(JPanel panel)
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        Border southBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.MEDIUM_GRAY_COLOR);
        wrapper.setBorder(southBorder);
        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private FilterCheckBox createHideF2PCheckBox()
    {
        FilterCheckBox check = new FilterCheckBox("Hide F2P");
        check.setLabelTooltip("Hide stars in free to play worlds");
        check.subscribe(this::onFilterValueChanged);
        check.setValue(starFilter.isHideF2P());

        return check;
    }

    private FilterCheckBox createHidePVPCheckBox()
    {
        FilterCheckBox check = new FilterCheckBox("Hide PVP");
        check.setLabelTooltip("Hide stars in player-vs-player worlds");
        check.subscribe(this::onFilterValueChanged);
        check.setValue(starFilter.isHidePVP());

        return check;
    }

    private FilterCheckBox createHideWildernessCheckbox()
    {
        FilterCheckBox check = new FilterCheckBox("Hide Wilderness");
        check.setLabelTooltip("Hide stars landed in the wilderness");
        check.subscribe(this::onFilterValueChanged);
        check.setValue(starFilter.isHideWilderness());

        return check;
    }

    private FilterComboBox<Integer> createTotalLevelComboBox()
    {
        Integer[] comboBoxValues = IntStream.of(StarFilter.TOTAL_LEVELS)
                .boxed()
                .toArray(Integer[]::new);

        FilterComboBox<Integer> combo = new FilterComboBox<>("Total level", comboBoxValues);
        combo.setLabelTooltip("Hide stars in total level worlds exceeding this value");
        combo.subscribe(this::onFilterValueChanged);
        combo.setValue(starFilter.getMaxTotalLevel());

        return combo;
    }

    private FilterSliderRangePair createStarTierSliders()
    {
        int minValue = StarFilter.MIN_STAR_TIER;
        int maxValue = StarFilter.MAX_STAR_TIER;

        FilterSliderRangePair sliderPair = new FilterSliderRangePair(
                minValue, maxValue, minValue, maxValue, "Min tier: ", "Max tier: ");

        FilterSlider leftSlider = sliderPair.getFilterSliderMin();
        leftSlider.setLabelTooltip("Hide stars below this tier");
        leftSlider.subscribe(this::onFilterValueChanged);
        leftSlider.setValue(starFilter.getMinTier());


        FilterSlider rightSlider = sliderPair.getFilterSliderMax();
        rightSlider.setLabelTooltip("Hide stars above this tier");
        rightSlider.subscribe(this::onFilterValueChanged);
        rightSlider.setValue(starFilter.getMaxTier());

        return sliderPair;
    }

    private FilterSlider createMaxStarAgeSlider()
    {
        FilterSlider slider = new FilterSlider(
                StarFilter.MIN_MINUTES,
                StarFilter.MAX_MINUTES,
                (StarFilter.MAX_MINUTES - StarFilter.MIN_MINUTES) / 2,
                "Minutes: ");

        slider.setLabelTooltip("Hide stars that landed more than this many minutes ago");
        slider.subscribe(this::onFilterValueChanged);
        slider.setValue(starFilter.getMaxStarAge());

        return slider;
    }

    private void addContentElement(JComponent component)
    {
        component.setBackground(BACKGROUND_COLOR);
        contentPanel.add(component);
    }

    private void updateLayout()
    {
        wrappedContentPanel.setVisible(isExpanded);
        expandButton.setIcon(isExpanded ? SECTION_RETRACT_ICON : SECTION_EXPAND_ICON);
        expandButton.setRolloverIcon(isExpanded ? SECTION_RETRACT_ICON_HOVER : SECTION_EXPAND_ICON_HOVER);
        expandButton.setToolTipText(isExpanded ? "Retract" : "Expand");
    }

    private void onFilterValueChanged(Object target, Object value)
    {
        if (target == checkBoxHideF2P)
        {
            starFilter.setHideF2PWorlds((boolean) value);
        }
        else if (target == checkBoxHidePVP)
        {
            starFilter.setHidePVPWorlds((boolean) value);
        }
        else if (target == checkBoxHideWilderness)
        {
            starFilter.setHideWilderness((boolean) value);
        }
        else if (target == comboTotalLevel)
        {
            starFilter.setMaxTotalLevel((int) value);
        }
        else if (target == sliderStarTierMin)
        {
            starFilter.setMinTier((int) value);
        }
        else if (target == sliderStarTierMax)
        {
            starFilter.setMaxTier((int) value);
        }
        else if (target == sliderMaxStarAge)
        {
            starFilter.setMaxStarAge((int) value);
        }

        if (filterChangedCallback != null)
            filterChangedCallback.run();
    }

    public void setFilterChangedCallback(Runnable callback)
    {
        filterChangedCallback = callback;
    }
}
