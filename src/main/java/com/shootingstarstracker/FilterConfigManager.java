package com.shootingstarstracker;

import net.runelite.client.config.ConfigManager;

import javax.inject.Inject;

// Uses ConfigManager to save filter settings. 
// These are hidden config values and do not appear in the config panel.
public class FilterConfigManager
{
    @Inject
    private ConfigManager configManager;

    private final String pluginName;

    public FilterConfigManager(String pluginName)
    {
        this.pluginName = pluginName;
    }

    public String load(FilterKey key)
    {
        return configManager.getConfiguration(pluginName, key.toString());
    }

    public void save(FilterKey key, Object value)
    {
        configManager.setConfiguration(pluginName, key.toString(), value.toString());
    }

    public void initializeDefaultValue(FilterKey key, Object defaultValue)
    {
        if (load(key) == null)
            save(key, defaultValue);
    }
}
