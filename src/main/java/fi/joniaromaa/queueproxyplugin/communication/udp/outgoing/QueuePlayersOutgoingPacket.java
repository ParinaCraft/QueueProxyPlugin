package fi.joniaromaa.queueproxyplugin.communication.udp.outgoing;

import java.util.LinkedHashSet;
import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.communication.udp.UdpOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class QueuePlayersOutgoingPacket implements UdpOutgoingPacket
{
	private static final int PACKET_ID = 1;
	
	private int queueId;
	private int gameType;
	private LinkedHashSet<UUID> uuids; //We want to keep order, the first player on the list is always considered as the host, also this is hashset so we can avoid dublicate uuids
	
	public QueuePlayersOutgoingPacket(int queueId, int gameType, UUID... uuids)
	{
		this.queueId = queueId;
		this.gameType = gameType;
		this.uuids = new LinkedHashSet<>();
		for(UUID uuid : uuids)
		{
			this.uuids.add(uuid);
		}
	}
	
	public QueuePlayersOutgoingPacket(int queueId, int gameType, LinkedHashSet<UUID> uuids)
	{
		this.queueId = queueId;
		this.gameType = gameType;
		this.uuids = uuids;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte(QueuePlayersOutgoingPacket.PACKET_ID);
		buf.writeInt(this.queueId);
		buf.writeByte(this.gameType);
		buf.writeByte(this.uuids.size());
		for (UUID uuid : this.uuids)
		{
			ByteBufUtils.writeUUID(buf, uuid);
		}
		return buf;
	}
}
