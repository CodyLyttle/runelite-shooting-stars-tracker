package com.shootingstarstracker.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.shootingstarstracker.models.ShootingStar;
import com.shootingstarstracker.models.ShootingStarDTO;
import net.runelite.api.Client;
import net.runelite.client.game.WorldService;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShootingStarsFetcher
{
    @Inject
    Client client;

    @Inject
    WorldService worldService;

    public List<ShootingStar> fetchStars()
    {
        Gson gson = new Gson();
        List<ShootingStarDTO> shootingStarDTOs = null;

        try
        {
            URL url = new URL("https://osrsportal.com/activestars");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Referer", "https://osrsportal.com/shooting-stars-tracker");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                shootingStarDTOs = gson.fromJson(reader, new TypeToken<List<ShootingStarDTO>>()
                {
                }.getType());
                reader.close();
            }
            else
            {
                System.out.println("Failed to fetch data. HTTP Error code: " + responseCode);
            }

            connection.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (shootingStarDTOs == null)
            return new ArrayList<>();

        return shootingStarDTOs.stream()
                .map(this::convertToModel)
                // Order by most recent.
                .sorted(Comparator.comparingInt(shootingStar -> shootingStar != null ? shootingStar.getMinutesAgo() : 0))
                .collect(Collectors.toList());
    }

    private ShootingStar convertToModel(ShootingStarDTO dto)
    {
        WorldResult worldResult = worldService.getWorlds();
        assert worldResult != null;

        World world = worldResult.findWorld(dto.getWorld());
        if (world == null)
            return null;

        final net.runelite.api.World starWorld = client.createWorld();
        starWorld.setActivity(world.getActivity());
        starWorld.setAddress(world.getAddress());
        starWorld.setId(dto.getWorld());
        starWorld.setPlayerCount(world.getPlayers());
        starWorld.setLocation(world.getLocation());
        starWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));


        return new ShootingStar(dto.getTime(), dto.getTier(), starWorld, dto.getRegion(), dto.getLoc());
    }
}
