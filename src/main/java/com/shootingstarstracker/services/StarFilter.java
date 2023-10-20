package com.shootingstarstracker.services;

import com.shootingstarstracker.enums.FilterKey;
import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.Parser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.World;
import net.runelite.api.WorldType;

import javax.inject.Inject;
import java.util.EnumSet;

@Slf4j
public class StarFilter
{
    private final static boolean DEFAULT_HIDE_F2P = false;
    private final static boolean DEFAULT_HIDE_PVP = true;
    private final static boolean DEFAULT_HIDE_WILDERNESS = true;
    private final static int DEFAULT_TOTAL_LEVEL = 1750;
    private final static int DEFAULT_MIN_TIER = 3;
    private final static int DEFAULT_MAX_TIER = 6;
    private final static int DEFAULT_MAX_STAR_AGE = 35;

    public final static int MIN_STAR_TIER = 1;
    public final static int MAX_STAR_TIER = 9;
    public final static int MIN_MINUTES = 0;
    public final static int MAX_MINUTES = 120;
    public final static int[] TOTAL_LEVELS = {0, 500, 750, 1250, 1500, 1750, 2000, 2200};

    @Getter
    private boolean hideF2P;

    @Getter
    private boolean hidePVP;

    @Getter
    private boolean hideWilderness;

    @Getter
    private int maxTotalLevel;

    @Getter
    private int minTier;

    @Getter
    private int maxTier;

    @Getter
    private int maxStarAge;

    private final IFilterConfigManager filterConfigManager;

    @Inject
    public StarFilter(IFilterConfigManager filterConfigManager)
    {
        this.filterConfigManager = filterConfigManager;
        hideF2P = readInitialBool(FilterKey.HIDE_F2P, DEFAULT_HIDE_F2P);
        hidePVP = readInitialBool(FilterKey.HIDE_PVP, DEFAULT_HIDE_PVP);
        hideWilderness = readInitialBool(FilterKey.HIDE_WILDERNESS, DEFAULT_HIDE_WILDERNESS);
        maxTotalLevel = readInitialInt(FilterKey.TOTAL_LEVEL, DEFAULT_TOTAL_LEVEL);
        minTier = readInitialInt(FilterKey.MIN_TIER, DEFAULT_MIN_TIER);
        maxTier = readInitialInt(FilterKey.MAX_TIER, DEFAULT_MAX_TIER);
        maxStarAge = readInitialInt(FilterKey.MAX_STAR_AGE, DEFAULT_MAX_STAR_AGE);
    }

    public boolean meetsCriteria(ShootingStar star)
    {
        boolean isFiltered = star.getTier() < minTier
                || star.getTier() > maxTier
                || star.getMinutesAgo() > maxStarAge
                || isFilteredWorld(star.getWorld())
                || star.getRegion().toLowerCase().contains("wildy");

        return !isFiltered;
    }

    private boolean isFilteredWorld(World world)
    {
        EnumSet<WorldType> worldTypes = world.getTypes();

        if (hideF2P && !worldTypes.contains(WorldType.MEMBERS))
            return true;

        if (hidePVP && WorldType.isPvpWorld(worldTypes))
            return true;

        if (worldTypes.contains(WorldType.SKILL_TOTAL))
        {
            try
            {
                int totalLevel = Parser.totalLevelNumber(world);
                ;
                if (totalLevel > maxTotalLevel)
                    return true;
            }
            catch (NumberFormatException e)
            {
                log.error("Failed to parse total level integer from total level world");
            }
        }

        return false;
    }

    private boolean readInitialBool(FilterKey key, boolean defaultValue)
    {
        return Boolean.parseBoolean(filterConfigManager.readExistingOrWriteDefault(key, defaultValue));
    }

    private int readInitialInt(FilterKey key, int defaultValue)
    {
        return Integer.parseInt(filterConfigManager.readExistingOrWriteDefault(key, defaultValue));
    }

    public void setHideF2PWorlds(boolean value)
    {
        if (trySaveChanges(hideF2P, value, FilterKey.HIDE_F2P))
            hideF2P = value;
    }

    public void setHidePVPWorlds(boolean value)
    {
        if (trySaveChanges(hidePVP, value, FilterKey.HIDE_PVP))
            hidePVP = value;
    }

    public void setHideWilderness(boolean value)
    {
        if (trySaveChanges(hideWilderness, value, FilterKey.HIDE_WILDERNESS))
            hideWilderness = value;
    }

    public void setMaxTotalLevel(int value)
    {
        if (trySaveChanges(maxTotalLevel, value, FilterKey.TOTAL_LEVEL))
            maxTotalLevel = value;
    }

    public void setMinTier(int value)
    {
        if (trySaveChanges(minTier, value, FilterKey.MIN_TIER))
            minTier = value;
    }

    public void setMaxTier(int value)
    {
        if (trySaveChanges(maxTier, value, FilterKey.MAX_TIER))
            maxTier = value;
    }

    public void setMaxStarAge(int value)
    {
        if (trySaveChanges(maxStarAge, value, FilterKey.MAX_STAR_AGE))
            maxStarAge = value;
    }

    private <T> boolean trySaveChanges(T oldValue, T newValue, FilterKey key)
    {
        if (oldValue == newValue)
            return false;

        filterConfigManager.write(key, newValue);
        return true;
    }
}
