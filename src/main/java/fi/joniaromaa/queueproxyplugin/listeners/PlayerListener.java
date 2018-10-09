package fi.joniaromaa.queueproxyplugin.listeners;

import java.io.IOException;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerListener implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerKickEvent(ServerKickEvent event) throws IOException
	{
		if (!event.getKickedFrom().getName().equalsIgnoreCase("lobby"))
		{
			event.setCancelled(true);
			event.setCancelServer(QueueProxyPlugin.getPlugin().getProxy().getServerInfo("lobby"));
			
			event.getPlayer().sendMessage(new TextComponent(ChatColor.RED + "Sinut potkittiin pihalle palvelimelta " + event.getKickedFrom().getName() + " syystä: " + event.getKickReason()));
		}
	}
}
