package fi.joniaromaa.queueproxyplugin.duels;

import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.game.GameType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DuelRequest
{
	@Getter private final UUID challanger;
	@Getter private final String challangerUsername;
	
	@Getter private final UUID target;
	@Getter private final String targetUsername;
	
	@Getter private final String mode;
	@Getter private final GameType gametype;
}
