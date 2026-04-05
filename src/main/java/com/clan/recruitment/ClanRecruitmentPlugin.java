package com.clan.recruitment;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(
	name = "Clan Recruitment",
	description = "Hide players when Recruit is clicked",
	tags = {"clan", "recruit", "hide"}
)
public class ClanRecruitmentPlugin extends Plugin
{
	@Inject
	private ClanRecruitmentConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Hooks hooks;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ClanRecruitmentPanel panel;

	private NavigationButton navButton;

	private final Set<String> hiddenPlayers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Provides
	ClanRecruitmentConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ClanRecruitmentConfig.class);
	}

	@Override
	protected void startUp()
	{
		loadHiddenPlayers();

		navButton = NavigationButton.builder()
			.tooltip("Clan Recruitment")
			.icon(createIcon())
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
		hooks.registerRenderableDrawListener(drawListener);
		panel.refresh(hiddenPlayers);
	}

	@Override
	protected void shutDown()
	{
		hooks.unregisterRenderableDrawListener(drawListener);
		clientToolbar.removeNavigation(navButton);
		navButton = null;
		hiddenPlayers.clear();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!config.enabled())
		{
			return;
		}

		if (!"Recruit".equalsIgnoreCase(event.getMenuOption()))
		{
			return;
		}

		if (event.getMenuEntry() == null || event.getMenuEntry().getPlayer() == null)
		{
			return;
		}

		Player player = event.getMenuEntry().getPlayer();
		String playerName = normalizeName(player.getName());

		if (playerName.isEmpty())
		{
			return;
		}

		if (hiddenPlayers.add(playerName))
		{
			saveHiddenPlayers();
			panel.refresh(hiddenPlayers);
			log.debug("Added hidden player: {}", playerName);
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!ClanRecruitmentConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}

		if ("hiddenPlayers".equals(event.getKey()))
		{
			loadHiddenPlayers();
			panel.refresh(hiddenPlayers);
		}
		else if ("enabled".equals(event.getKey()))
		{
			panel.refresh(hiddenPlayers);
		}
	}

	boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		if (!(renderable instanceof Player))
		{
			return true;
		}

		if (!config.enabled())
		{
			return true;
		}

		Player player = (Player) renderable;
		String playerName = normalizeName(player.getName());

		if (playerName.isEmpty())
		{
			return true;
		}

		return !hiddenPlayers.contains(playerName);
	}

	void setFeatureEnabled(boolean enabled)
	{
		configManager.setConfiguration(ClanRecruitmentConfig.GROUP, "enabled", enabled);
		panel.refresh(hiddenPlayers);
	}

	void clearHiddenPlayers()
	{
		hiddenPlayers.clear();
		saveHiddenPlayers();
		panel.refresh(hiddenPlayers);
	}

	Collection<String> getHiddenPlayers()
	{
		return hiddenPlayers;
	}

	private void loadHiddenPlayers()
	{
		hiddenPlayers.clear();

		String raw = config.hiddenPlayers();
		if (raw == null || raw.trim().isEmpty())
		{
			return;
		}

		for (String part : raw.split("\\n"))
		{
			String name = normalizeName(part);
			if (!name.isEmpty())
			{
				hiddenPlayers.add(name);
			}
		}
	}

	private void saveHiddenPlayers()
	{
		String joined = hiddenPlayers.stream()
			.sorted(String.CASE_INSENSITIVE_ORDER)
			.collect(Collectors.joining("\n"));

		configManager.setConfiguration(ClanRecruitmentConfig.GROUP, "hiddenPlayers", joined);
	}

	private static String normalizeName(String name)
	{
		if (name == null)
		{
			return "";
		}

		return name
			.replace('\u00A0', ' ')
			.trim();
	}

	private static BufferedImage createIcon()
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		try
		{
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.setColor(new Color(40, 40, 40, 220));
			g.fillRoundRect(0, 0, 16, 16, 4, 4);

			g.setColor(new Color(220, 220, 220));
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
			g.drawString("CR", 1, 12);
		}
		finally
		{
			g.dispose();
		}

		return image;
	}
}