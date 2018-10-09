package fi.joniaromaa.queueproxyplugin.commands;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SpectateCommand extends Command
{
	public SpectateCommand()
	{
		super("spectate");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer)sender;
			
			if (args.length == 1)
			{
				ProxiedPlayer target = QueueProxyPlugin.getPlugin().getProxy().getPlayer(args[0]);
				if (target != null)
				{
					if (QueueProxyPlugin.getPlugin().getNetworkManager().spectatePlayer(player.getUniqueId(), target.getUniqueId()))
					{
						sender.sendMessage(new TextComponent("Etsitään pelaajaa..."));
					}
					else
					{
						sender.sendMessage(new TextComponent("Etsimme jo pelaajaa..."));
					}
				}
				else
				{
					sender.sendMessage(new TextComponent("Pelaajaa ei löydetty"));
				}
			}
			else
			{
				sender.sendMessage(new TextComponent("/spectate <nimi>"));
			}
		}
		else
		{
			sender.sendMessage(new TextComponent("Only players may use this command!"));
		}
	}
}
