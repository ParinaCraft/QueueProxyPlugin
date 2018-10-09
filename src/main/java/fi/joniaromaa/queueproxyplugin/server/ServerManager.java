package fi.joniaromaa.queueproxyplugin.server;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;

public class ServerManager
{
	private Map<Integer, QueueServer> servers;
	
	public ServerManager()
	{
		this.servers = new HashMap<>();
	}
	
	@SuppressWarnings("deprecation")
	public void load() throws SQLException
	{
		try(Connection connection = QueueProxyPlugin.getPlugin().getDatabaseManager().getConnection(); Statement statement = connection.createStatement())
		{
			try(ResultSet result = statement.executeQuery("SELECT id, INET_NTOA(ip) as ip, port, name FROM servers"))
			{
				while (result.next())
				{
					QueueServer server = new QueueServer(result.getInt("id"), InetSocketAddress.createUnresolved(result.getString("ip"), result.getInt("port")), result.getString("name"));
					
					this.servers.put(server.getId(), server);
					
					QueueProxyPlugin.getPlugin().getProxy().getConfig().addServer(server.getServer());
				}
			}
		}
		
		/*
		//What we do here is edit our BungeeMSG plugin
		try
		{
			Class<?> storageManagerClass = Class.forName("me.O_o_Fadi_o_O.BungeeMSG.managers.StorageManager");
			Field serverNamesFilder = storageManagerClass.getDeclaredField("servernames");
			serverNamesFilder.setAccessible(true);
			
			HashMap<ServerInfo, String> servernames = (HashMap<ServerInfo, String>)serverNamesFilder.get(null);
			for(QueueServer server : this.servers.values())
			{
				servernames.put(server.getServer(), server.getName());
			}
		}
		catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}*/
	}
	
	public QueueServer getServer(int id)
	{
		return this.servers.get(id);
	}
}
