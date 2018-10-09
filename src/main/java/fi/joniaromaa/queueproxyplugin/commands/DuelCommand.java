package fi.joniaromaa.queueproxyplugin.commands;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.game.GameType;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class DuelCommand extends Command
{
	public DuelCommand()
	{
		super("duel");
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer)sender;
			
			if (args.length > 0)
			{
				switch(args[0])
				{
					case "help":
					{
						player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.help"));
						break;
					}
					case "modes":
					{
						player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.modes"));
						break;
					}
					case "accept":
					{
						if (args.length == 2)
						{
							ProxiedPlayer target = QueueProxyPlugin.getPlugin().getProxy().getPlayer(args[1]);
							if (target != null)
							{
								if (QueueProxyPlugin.getPlugin().getDuelsManager().acceptChallange(player, target))
								{
									player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.accept.done", target.getName()));
									target.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.accept.accepted", player.getName()));
								}
								else
								{
									player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.accept.failed", args[1]));
								}
							}
							else
							{
								player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.accept.target-not-found", args[1]));
							}
						}
						else
						{
							player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.accept.invalid-usage"));
						}
						break;
					}
					case "challange":
					default:
					{
						boolean showHelp = true;
						if (args[0].equals("challange")) //SPECIAL CASE
						{
							showHelp = false;
							
							String[] args_ = new String[args.length - 1];
							for (int i = 0; i < args.length - 1; i++)
							{
								args_[i] = args[i + 1];
							}
							
							args = args_;
						}
						
						if (args.length == 2)
						{
							ProxiedPlayer target = QueueProxyPlugin.getPlugin().getProxy().getPlayer(args[0]);
							if (target != null)
							{
								if (!target.getUniqueId().equals(player.getUniqueId()))
								{
									GameType gameType = QueueProxyPlugin.getPlugin().getGameTypeManager().getGameType("duels_1v1_" + args[1]);
									if (gameType != null)
									{
										if (QueueProxyPlugin.getPlugin().getDuelsManager().challangePlayer(player, target, args[1], gameType))
										{
											player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.done", args[0], args[1]));
										}
										else
										{
											player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.waiting-response", args[0]));
										}
									}
									else
									{
										player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.unknown-gametype", args[1]));
									}
								}
								else
								{
									player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.self"));
								}
							}
							else
							{
								player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.target-not-found", args[0]));
							}
						}
						else
						{
							if (showHelp || args.length > 2)
							{
								player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.help"));
							}
							else
							{
								player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.invalid-usage"));
							}
						}
						
						break;
					}
				}
			}
			else
			{
				player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.help"));
			}
		}
		else
		{
			sender.sendMessage(new TextComponent("Only players may use this command!"));
		}
	}
}
