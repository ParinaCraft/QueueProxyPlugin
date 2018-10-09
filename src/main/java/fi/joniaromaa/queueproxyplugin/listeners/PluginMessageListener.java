package fi.joniaromaa.queueproxyplugin.listeners;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.game.GameType;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PluginMessageListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPluginMessageEvent(PluginMessageEvent event) throws IOException
	{
		if (event.isCancelled())
		{
			return;
		}
		
		if (event.getTag().equalsIgnoreCase("Queue"))
		{
			event.setCancelled(true);
			
			if (event.getReceiver() instanceof ProxiedPlayer)
			{
				ProxiedPlayer player = (ProxiedPlayer)event.getReceiver();
				
				if (event.getSender() instanceof Server)
				{
					ByteArrayDataInput data = ByteStreams.newDataInput(event.getData());
					String queueId = data.readUTF();
					
					GameType gameType = QueueProxyPlugin.getPlugin().getGameTypeManager().getGameType(queueId);
					if (gameType != null)
					{
						if (QueueProxyPlugin.getPlugin().getNetworkManager().requestForQueue(gameType.getId(), player.getUniqueId()))
						{
							player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.queued", gameType.getName()));
						}
						else
						{
							player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.in-queue"));
						}
					}
					else
					{
						player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.unknown-gametype", queueId));
					}
				}
			}
		}
	}
}
