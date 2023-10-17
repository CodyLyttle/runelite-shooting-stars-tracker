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
    private int minutesAgo;

    @Getter
    private int tier;
    
    @Getter
    private World world;

    @Getter
    private String region;

    @Getter
    private String location;
}
