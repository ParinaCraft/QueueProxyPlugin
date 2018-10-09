package fi.joniaromaa.queueproxyplugin.game;

import lombok.Getter;

public class GameType
{
	@Getter private final int id;
	@Getter private final String name;
	@Getter private final String queueId;
	
	public GameType(int id, String name, String queueId)
	{
		this.id = id;
		this.name = name;
		this.queueId = queueId;
	}
}
