package com.shootingstarstracker.services;

import com.shootingstarstracker.enums.FilterKey;
import net.runelite.client.config.ConfigManager;

import javax.inject.Inject;

// Save/load plugin specific values that shouldn't appear in the config panel.
public class HiddenConfig implements IFilterConfigManager
{
    public final static String PLUGIN_NAME = "Shooting Stars Tracker";
    
    @Inject
    private ConfigManager configManager;

    public String read(FilterKey key)
    {
        return configManager.getConfiguration(PLUGIN_NAME, key.toString());
    }

    public void write(FilterKey key, Object value)
    {
        configManager.setConfiguration(PLUGIN_NAME, key.toString(), value.toString());
    }

    public String readExistingOrWriteDefault(FilterKey key, Object defaultValue)
    {
        String value = read(key);

        if (value == null)
        {
            write(key, defaultValue);
            value = read(key);
        }

        return value;
    }
}
