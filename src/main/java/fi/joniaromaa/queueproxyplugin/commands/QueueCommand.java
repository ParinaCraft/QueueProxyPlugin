package fi.joniaromaa.queueproxyplugin.commands;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.game.GameType;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class QueueCommand extends Command
{
	public QueueCommand()
	{
		super("queue");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer)sender;
			
			if (args.length > 0)
			{
				String queueId = String.join(" ", args);
				
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
			else
			{
				player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.missing-gametype"));
			}
		}
		else
		{
			sender.sendMessage(new TextComponent("Only players may use this command!"));
		}
	}
}
