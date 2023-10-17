package com.shootingstarstracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class StarsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(StarsPlugin.class);
		RuneLite.main(args);
	}
}