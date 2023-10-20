package com.shootingstarstracker.services;

import com.shootingstarstracker.enums.FilterKey;

public interface IFilterConfigManager
{
    String read(FilterKey key);

    void write(FilterKey key, Object value);

    String readExistingOrWriteDefault(FilterKey key, Object defaultValue);
}
