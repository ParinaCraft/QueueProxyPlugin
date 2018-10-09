package fi.joniaromaa.queueproxyplugin.server;

import java.net.InetSocketAddress;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;

public class QueueServer
{
	@Getter private final int id;
	@Getter private final InetSocketAddress address;
	@Getter private final String name;
	@Getter private final ServerInfo server;
	
	public QueueServer(int id, InetSocketAddress address, String name)
	{
		this.id = id;
		this.address = address;
		this.name = name;
		
		this.server = QueueProxyPlugin.getPlugin().getProxy().constructServerInfo(name, address, "QueueProxyPlugin Generated", false);
	}
}
