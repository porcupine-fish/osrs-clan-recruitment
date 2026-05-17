# Clan Recruitment Plugin
- Hides players you have already tried to recruit using the "Recruit" player menu option.
- This prevents spamming the same people with clan invites
- Also captures invites that only post in the game chat and not the clan chat.

## Optional webhook JSON payload
```
{
  "type": "CLAN_INVITATION_ACCEPTING",
  "playerName": "PlayerX",
  "message": "PlayerX is attempting to accept your invitation!"
}
```