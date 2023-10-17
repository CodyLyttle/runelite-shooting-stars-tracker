package com.shootingstarstracker.models;

import lombok.Getter;
import lombok.Setter;

public class ShootingStar
{
    @Getter
    @Setter
    private String region;

    @Getter
    @Setter
    private int time;

    @Getter
    @Setter
    private int world;

    @Getter
    @Setter
    private int tier;

    @Getter
    @Setter
    private String loc;

    @Getter
    @Setter
    private String scout;

    public String ToMessageString()
    {
        String msg = "";
        msg += "Tier: " + tier + "\r\n";
        msg += "Time: " + time + " mins ago\r\n";
        msg += "World: " + world + "\r\n";
        msg += "Region: " + region + "\r\n";
        msg += "Location: " + loc + "\r\n";
        msg += "Scout: " + scout + "\r\n";
        
        return msg;
    }
}
