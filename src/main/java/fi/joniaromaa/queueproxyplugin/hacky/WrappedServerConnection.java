package fi.joniaromaa.queueproxyplugin.hacky;

import lombok.Getter;
import net.md_5.bungee.ServerConnection;

public class WrappedServerConnection extends ServerConnection
{
	@Getter private final ServerConnection serverConnection;
	
	public WrappedServerConnection(ServerConnection serverConnection)
	{
		super(serverConnection.getCh(), new WrappedBungeeServerInfo(serverConnection.getInfo()));
		
		this.serverConnection = serverConnection;
	}
	
	@Override
	public void setObsolete(boolean obsolete)
	{
		this.serverConnection.setObsolete(obsolete);
	}
	
	@Override
	public boolean isObsolete()
	{
		return this.serverConnection.isObsolete();
	}
	
	@Override
	public boolean isForgeServer()
	{
		return this.serverConnection.isForgeServer();
	}
	
	@Override
	public long getSentPingId()
	{
		return this.serverConnection.getSentPingId();
	}
	
	@Override
	public void setSentPingId(long sentPingId)
	{
		this.serverConnection.setSentPingId(sentPingId);
	}
}
