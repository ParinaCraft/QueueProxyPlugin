package fi.joniaromaa.queueproxyplugin.duels;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.game.GameType;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DuelsManager
{
	private Cache<UUID, DuelRequest> pendingDuels;
	
	public DuelsManager()
	{
		this.pendingDuels = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, DuelRequest>()
		{
			@Override
			public void onRemoval(RemovalNotification<UUID, DuelRequest> notif)
			{
				DuelRequest request = notif.getValue();
				if (notif.getCause() == RemovalCause.EXPIRED)
				{
					ProxiedPlayer player = QueueProxyPlugin.getPlugin().getProxy().getPlayer(request.getChallanger());
					if (player != null)
					{
						player.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.request-expired-sender", request.getTargetUsername(), request.getMode()));
					}
					
					ProxiedPlayer target = QueueProxyPlugin.getPlugin().getProxy().getPlayer(request.getTarget());
					if (target != null)
					{
						target.sendMessage(LangUtils.getText(player.getLocale(), "command.duel.challange.request-expired-target", request.getChallangerUsername(), request.getMode()));
					}
				}
			}
		}).build();
	}
	
	public boolean challangePlayer(ProxiedPlayer challanger, ProxiedPlayer target, String mode, GameType gametype)
	{
		DuelRequest pendingRequest = this.pendingDuels.getIfPresent(challanger.getUniqueId());
		if (pendingRequest == null)
		{
			this.pendingDuels.put(challanger.getUniqueId(), new DuelRequest(challanger.getUniqueId(), challanger.getName(), target.getUniqueId(), target.getName(), mode, gametype));
			
			TextComponent message = new TextComponent(LangUtils.getText(target.getLocale(), "command.duel.challange.receive", challanger.getName(), mode));
			
			TextComponent clickHere = new TextComponent("KLIKKAA TÄSTÄ");
			clickHere.setColor(ChatColor.GREEN);
			clickHere.setBold(true);
			clickHere.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/duel accept " + challanger.getName()).create()));
			clickHere.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + challanger.getName()));
			
			message.addExtra(" ");
			message.addExtra(clickHere);
			message.addExtra(" hyväksyäksesi kaksintaistelu haasteen");
			
			target.sendMessage(message);
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean acceptChallange(ProxiedPlayer player, ProxiedPlayer challanger)
	{
		DuelRequest request = this.pendingDuels.getIfPresent(challanger.getUniqueId());
		if (request != null)
		{
			this.pendingDuels.invalidate(challanger.getUniqueId());
			
			QueueProxyPlugin.getPlugin().getNetworkManager().requestForQueue(request.getGametype().getId(), challanger.getUniqueId(), player.getUniqueId());
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void onMonitor()
	{
		this.pendingDuels.cleanUp();
	}
}
