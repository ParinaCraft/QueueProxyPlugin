package fi.joniaromaa.queueproxyplugin.hacky;

import java.util.Collection;
import java.util.Objects;

import com.google.common.base.Preconditions;

import lombok.Getter;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.packet.PluginMessage;

public class WrappedBungeeServerInfo extends BungeeServerInfo
{
	@Getter private final BungeeServerInfo serverInfo;
	
	public WrappedBungeeServerInfo(BungeeServerInfo serverInfo)
	{
		super(serverInfo.getName(), serverInfo.getAddress(), serverInfo.getMotd(), serverInfo.isRestricted());
		
		this.serverInfo = serverInfo;
	}
	
    @Override
    public void addPlayer(ProxiedPlayer player)
    {
        this.serverInfo.addPlayer(player);
    }

    @Override
    public void removePlayer(ProxiedPlayer player)
    {
        this.serverInfo.removePlayer(player);
    }
    
    @Override
    public Collection<ProxiedPlayer> getPlayers()
    {
    	return this.serverInfo.getPlayers();
    }
    
    @Override
    public boolean sendData(String channel, byte[] data, boolean queue)
    {
        Preconditions.checkNotNull(channel, "channel");
        Preconditions.checkNotNull(data, "data");

        synchronized (this.serverInfo.getPacketQueue())
        {
        	Collection<ProxiedPlayer> players = this.getPlayers();
        	
            Server server = (players.isEmpty()) ? null : players.iterator().next().getServer();
            if (server != null)
            {
                server.sendData(channel, data);
                return true;
            }
            else if (queue)
            {
            	this.serverInfo.getPacketQueue().add(new PluginMessage(channel, data, false));
            }
            
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof WrappedBungeeServerInfo) && Objects.equals(getAddress(), ((ServerInfo) obj).getAddress());
    }

    @Override
    public int hashCode()
    {
        return this.getAddress().hashCode() + 1;
    }
}
