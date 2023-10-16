package com.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.models.ShootingStar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ActiveStarsFetcher
{
    public List<ShootingStar> fetchShootingStars()
    {
        Gson gson = new Gson();
        List<ShootingStar> shootingStars = null;

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
                shootingStars = gson.fromJson(reader, new TypeToken<List<ShootingStar>>()
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
        catch (
                Exception e)

        {
            e.printStackTrace();
        }

        return shootingStars;
    }
}
