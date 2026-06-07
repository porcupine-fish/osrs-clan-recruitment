package com.clan.recruitment;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ClanRecruitmentPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ClanRecruitmentPlugin.class);
		RuneLite.main(new String[]{"--developer-mode", "--disable-telemetry"});
	}
}