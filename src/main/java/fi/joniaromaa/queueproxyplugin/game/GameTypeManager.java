package fi.joniaromaa.queueproxyplugin.game;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;

public class GameTypeManager
{
	private HashMap<Integer, GameType> gameTypesById;
	private HashMap<String, GameType> gameTypesByQueueId;
	
	public GameTypeManager()
	{
		this.gameTypesById = new HashMap<>();
		this.gameTypesByQueueId = new HashMap<>();
	}
	
	public void load() throws SQLException
	{
		try(Connection connection = QueueProxyPlugin.getPlugin().getDatabaseManager().getConnection(); Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery("SELECT id, name, queue_id FROM gametypes"))
		{
			while (result.next())
			{
				GameType gameType = new GameType(result.getInt("id"), result.getString("name"), result.getString("queue_id"));
				
				this.gameTypesById.put(gameType.getId(), gameType);
				this.gameTypesByQueueId.put(gameType.getQueueId(), gameType);
			}
		}
	}
	
	public GameType getGameType(int id)
	{
		return this.gameTypesById.get(id);
	}
	
	public GameType getGameType(String queueId)
	{
		return this.gameTypesByQueueId.get(queueId);
	}
}
