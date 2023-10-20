package com.shootingstarstracker.ui.elements;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class HyperlinkLabel extends JLabel
{
    private final static Color HYPERLINK_BLUE = new Color(30, 144, 255);
    private final static Color HYPERLINK_BLUE_HOVERED = new Color(60, 200, 238);

    private final URI uri;

    public HyperlinkLabel(String text, String address)
    {
        setForeground(HYPERLINK_BLUE);
        setFont(FontManager.getRunescapeSmallFont());
        setText(text);

        URI uriFromAddress = null;

        try
        {
            uriFromAddress = new URI(address);
        }
        catch (URISyntaxException e)
        {
            log.error("Failed to parse URI from address string '" + address + "'");
        }

        uri = uriFromAddress;

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (uri != null && Desktop.isDesktopSupported())
                {
                    try
                    {
                        Desktop.getDesktop().browse(uri);
                    }
                    catch (IOException ex)
                    {
                        log.warn("Default browser not found", ex);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                setForeground(HYPERLINK_BLUE_HOVERED);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setForeground(HYPERLINK_BLUE);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
