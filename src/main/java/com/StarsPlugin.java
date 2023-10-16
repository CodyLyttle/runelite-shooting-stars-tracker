package com;

import com.google.inject.Provides;
import javax.inject.Inject;

import com.models.ShootingStar;
import com.services.ActiveStarsFetcher;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Shooting Stars Tracker"
)
public class StarsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private StarsConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("'Shooting Stars Tracker' started!");
		ActiveStarsFetcher fetcher = new ActiveStarsFetcher();
		List<ShootingStar> stars = fetcher.fetchShootingStars();
		
		for (ShootingStar star : stars)
		{
			log.info(star.ToMessageString());
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("'Shooting Stars Tracker' stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "'Shooting Stars Tracker' says " + config.greeting(), null);
		}
	}

	@Provides
	StarsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(StarsConfig.class);
	}
}
