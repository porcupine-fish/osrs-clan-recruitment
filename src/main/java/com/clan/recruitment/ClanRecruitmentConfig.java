package com.clan.recruitment;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(ClanRecruitmentConfig.GROUP)
public interface ClanRecruitmentConfig extends Config
{
	String GROUP = "clanrecruitment";

	@ConfigItem(
		keyName = "enabled",
		name = "Enable hiding of players",
		description = "Hide players when their Recruit menu option is clicked",
		position = 0
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hiddenPlayers",
		name = "Hidden players list",
		description = "Persisted list of hidden player names"
	)
	default String hiddenPlayers()
	{
		return "";
	}
	@ConfigItem(
		keyName = "webhookUrl",
		name = "Webhook URL (optional)",
		description = "Optional URL to send recruitment acceptance events to"
	)
	default String webhookUrl()
	{
		return "";
	}
}