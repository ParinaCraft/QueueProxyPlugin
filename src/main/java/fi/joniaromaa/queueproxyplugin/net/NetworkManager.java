package fi.joniaromaa.queueproxyplugin.net;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import fi.joniaromaa.queueproxyplugin.communication.udp.UdpOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.communication.udp.UdpPacketManager;
import fi.joniaromaa.queueproxyplugin.communication.udp.outgoing.QueuePlayerOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.communication.udp.outgoing.QueuePlayersOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.communication.udp.outgoing.SpectatePlayerOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.utils.LangUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NetworkManager
{
	@Getter private UdpPacketManager udpPacketManager;
	
	private EventLoopGroup bossGroup;
	private Channel channel;
	
	private AtomicInteger nextQueueId;
	private Cache<Integer, UUID> queueIds;
	private Cache<Integer, HashSet<UUID>> multiQueueIds;
	private HashSet<UUID> playersOnQueue;
	  
	public NetworkManager()
	{
		this.udpPacketManager = new UdpPacketManager();
		
		this.bossGroup = new NioEventLoopGroup(1);
		
		this.nextQueueId = new AtomicInteger(1);
		this.queueIds = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS).removalListener(new RemovalListener<Integer, UUID>()
		{
			@Override
			public void onRemoval(RemovalNotification<Integer, UUID> notif)
			{
				UUID uuid = notif.getValue();
				if (notif.getCause() == RemovalCause.EXPIRED)
				{
					ProxiedPlayer player = QueueProxyPlugin.getPlugin().getProxy().getPlayer(uuid);
					if (player != null)
					{
						player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.request-expired"));
					}
				}
				
				NetworkManager.this.playersOnQueue.remove(uuid);
			}
		}).build();
		
		this.multiQueueIds = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS).removalListener(new RemovalListener<Integer, HashSet<UUID>>()
		{
			@Override
			public void onRemoval(RemovalNotification<Integer, HashSet<UUID>> notif)
			{
				HashSet<UUID> uuids = notif.getValue();
				if (notif.getCause() == RemovalCause.EXPIRED)
				{
					for(UUID uuid : uuids)
					{
						ProxiedPlayer player = QueueProxyPlugin.getPlugin().getProxy().getPlayer(uuid);
						if (player != null)
						{
							player.sendMessage(LangUtils.getText(player.getLocale(), "command.queue.request-expired"));
						}
					}
				}
				
				NetworkManager.this.playersOnQueue.removeAll(uuids);
			}
		}).build();
		
		this.playersOnQueue = new HashSet<>();
	}
	
	public int getNextQueueId()
	{
		return this.nextQueueId.getAndIncrement();
	}
	
	public void start() throws InterruptedException
	{
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(this.bossGroup)
		.channel(NioDatagramChannel.class)
		.option(ChannelOption.SO_BROADCAST, true)
		.handler(new UDPPacketDataHandler());
		
		this.channel = bootstrap.bind(0).sync().channel();
	}
	
	public void stop()
	{
		this.bossGroup.shutdownGracefully();
	}
	
	public boolean requestForQueue(int gameType, UUID uuid)
	{
		int queueId = this.getNextQueueId();
		
		if (this.playersOnQueue.add(uuid))
		{
			this.queueIds.put(queueId, uuid);
			
			this.sendPacket(new QueuePlayerOutgoingPacket(queueId, gameType, uuid));
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean spectatePlayer(UUID sender, UUID target)
	{
		int queueId = this.getNextQueueId();
		
		if (this.playersOnQueue.add(sender))
		{
			this.queueIds.put(queueId, sender);
			
			this.sendPacket(new SpectatePlayerOutgoingPacket(queueId, sender, target));
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Queues all the players and overrides and invalidates all other queues
	 * @param gameType
	 * @param uuids
	 */
	public void requestForQueue(int gameType, UUID... uuids)
	{
		int queueId = this.getNextQueueId();
		
		LinkedHashSet<UUID> uuids_ = new LinkedHashSet<UUID>();
		for(UUID uuid : uuids)
		{
			if (uuids_.add(uuid))
			{
				this.playersOnQueue.add(uuid);
			}
		}
		
		this.multiQueueIds.put(queueId, uuids_);
		
		this.sendPacket(new QueuePlayersOutgoingPacket(queueId, gameType, uuids_));
	}
	
	public void sendPacket(UdpOutgoingPacket packet)
	{
		this.channel.writeAndFlush(new DatagramPacket(packet.getBytes(), SocketUtils.socketAddress("127.0.0.1", 7650)));
	}
	
	public UUID queueResult(int queueId)
	{
		UUID uuid = this.queueIds.getIfPresent(queueId);
		if (uuid != null)
		{
			this.queueIds.invalidate(queueId);
		}
		
		return uuid;
	}
	
	public HashSet<UUID> multiQueueResult(int queueId)
	{
		HashSet<UUID> uuids = this.multiQueueIds.getIfPresent(queueId);
		if (uuids != null)
		{
			this.multiQueueIds.invalidate(queueId);
		}
		
		return uuids;
	}
	
	public void onMonitor()
	{
		this.queueIds.cleanUp();
		this.multiQueueIds.cleanUp();
	}
}
