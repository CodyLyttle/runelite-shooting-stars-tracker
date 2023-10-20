package com.shootingstarstracker;


import net.runelite.api.World;

public class Parser
{
    public static int totalLevelNumber(World world) throws NumberFormatException
    {
        String levelString = world.getActivity().split(" ", 2)[0];
        return Integer.parseInt(levelString);
    }

    public static String enumStringToCamelCase(String str)
    {
        char[] chars = new char[str.length()];
        boolean newWord = true;

        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);

            if (c == '_')
            {
                chars[i] = ' ';
                newWord = true;
            }
            else if (newWord && Character.isAlphabetic(c))
            {
                chars[i] = c;
                newWord = false;
            }
            else
            {
                chars[i] = Character.toLowerCase(c);
            }
        }

        return new String(chars);
    }
}
