package fi.joniaromaa.queueproxyplugin.communication.udp.incoming;

import java.util.HashSet;
import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.communication.udp.UdpIncomingPacket;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import io.netty.channel.socket.DatagramPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NoServersFoundIncomingPacket implements UdpIncomingPacket
{
	@Override
	public void handle(DatagramPacket msg)
	{
		int queueId = msg.content().readInt();
		
		UUID uuid = QueueProxyPlugin.getPlugin().getNetworkManager().queueResult(queueId);
		if (uuid != null)
		{
			this.sendError(uuid);
		}
		else
		{
			HashSet<UUID> uuids = QueueProxyPlugin.getPlugin().getNetworkManager().multiQueueResult(queueId);
			if (uuids != null)
			{
				for(UUID uuid_ : uuids)
				{
					this.sendError(uuid_);
				}
			}
		}
	}
	
	private void sendError(UUID uuid)
	{
		ProxiedPlayer player = QueueProxyPlugin.getPlugin().getProxy().getPlayer(uuid);
		if (player != null)
		{
			player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.no-servers"));
		}
	}
}
