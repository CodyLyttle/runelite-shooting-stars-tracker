package com.shootingstarstracker.models;

import lombok.Getter;
import net.runelite.api.World;

public class ShootingStar
{
    public ShootingStar(int minutesAgo, int tier, World world, String region, String location)
    {
        this.minutesAgo = minutesAgo;
        this.tier = tier;
        this.world = world;
        this.region = region;
        this.location = location;
    }
    
    @Getter
    private final int minutesAgo;

    @Getter
    private final int tier;
    
    @Getter
    private final World world;

    @Getter
    private final String region;

    @Getter
    private final String location;
}
