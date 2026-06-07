# Clan Recruitment plugin
A tiny plugin to optimise clan recruitment and to capture clan recruitment game messages that don't appear in the clan chat.

## How recruiting currently works without this plugin
1) When you have the clan Vexillium (clan flag) equipped, the "Recruit" menu option is visible for players.
2) When you click the Recruit menu button, the target player gets a dialog confirmation to join the clan.
3) Sometimes there is a game message that reads: `"{PlayerX} is attempting to accept your invitation!"`
3.1) This doesn't get captured in the clan chat, which makes collecting recruiter stats very difficult; as there is missing information.

## Plugin Features
- When you click the "Recruit" player menu button, the player is hidden (similar to Entity hider plugin).
  - This stops the same people getting spammed with invites
  - This reduces unnecessary API calls to Jagex
- There is a plugin panel to control whether the hiding of players is enabled and also a listbox of player usernames that have been hidden.
- Optionally, you can specify a webhook URL to capture the game message: `"{PlayerX} is attempting to accept your invitation!"`

## Attempting to accept game message, webhook JSON Payload shape 
```
{
  "type": "CLAN_INVITATION_ACCEPTING",
  "playerName": "PlayerX",
  "message": "PlayerX is attempting to accept your invitation!"
}
```

## Config
```
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
```