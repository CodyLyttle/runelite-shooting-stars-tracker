package com.shootingstarstracker.models;

import lombok.Getter;
import lombok.Setter;

public class ShootingStarDTO
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
}
