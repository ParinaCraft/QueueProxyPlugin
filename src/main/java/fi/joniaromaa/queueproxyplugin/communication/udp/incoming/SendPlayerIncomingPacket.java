package fi.joniaromaa.queueproxyplugin.communication.udp.incoming;

import java.util.HashSet;
import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.communication.udp.UdpIncomingPacket;
import fi.joniaromaa.queueproxyplugin.hacky.WrappedServerConnection;
import fi.joniaromaa.queueproxyplugin.server.QueueServer;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import io.netty.channel.socket.DatagramPacket;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendPlayerIncomingPacket implements UdpIncomingPacket
{
	@Override
	public void handle(DatagramPacket msg)
	{
		int queueId = msg.content().readInt();
		int serverId = msg.content().readInt();
		
		UUID uuid = QueueProxyPlugin.getPlugin().getNetworkManager().queueResult(queueId);
		if (uuid != null)
		{
			this.connectToServer(uuid, serverId);
		}
		else
		{
			HashSet<UUID> uuids = QueueProxyPlugin.getPlugin().getNetworkManager().multiQueueResult(queueId);
			if (uuids != null)
			{
				for(UUID uuid_ : uuids)
				{
					this.connectToServer(uuid_, serverId);
				}
			}
		}
	}
	
	private void connectToServer(UUID uuid, int serverId)
	{
		ProxiedPlayer player = QueueProxyPlugin.getPlugin().getProxy().getPlayer(uuid);
		if (player != null)
		{
			QueueServer queueServer = QueueProxyPlugin.getPlugin().getServerManager().getServer(serverId);
			if (queueServer != null)
			{
				player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.server-found"));
				
				if (player.getServer() != null && queueServer.getServer().equals(player.getServer().getInfo()))
				{
					//Now we start hacking around the bungee!! This is really, really, really bad idea xD
					
					if (player instanceof UserConnection)
					{
						UserConnection userConnection = (UserConnection)player;
						ServerConnection oldServer = userConnection.getServer();
						
						oldServer.setObsolete(true); //Make it obsolate so we don't get disconnected
						oldServer.disconnect("Reconnect"); //Disconnect manually to make sure the connection is disconnected
						
						userConnection.setServer(new WrappedServerConnection(oldServer)); //Mess with this so we are allowed to connect back, we wrap the old one so that should be fine
						userConnection.connect(queueServer.getServer()); //Now we actually connect and hope for the best :s
					}
					else
					{
						//Oohh....
						player.disconnect(new TextComponent("You dont seem to work like how I want you would"));
					}
				}
				else
				{
					player.connect(queueServer.getServer());
				}
			}
			else
			{
				player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.server-found-unknown"));
			}
		}
	}
}
