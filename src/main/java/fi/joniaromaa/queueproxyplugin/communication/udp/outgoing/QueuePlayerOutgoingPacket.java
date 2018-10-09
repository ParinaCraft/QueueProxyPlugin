package fi.joniaromaa.queueproxyplugin.communication.udp.outgoing;

import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.communication.udp.UdpOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class QueuePlayerOutgoingPacket implements UdpOutgoingPacket
{
	private static final int PACKET_ID = 0;
	
	private int queueId;
	private int gameType;
	private UUID uuid;
	
	public QueuePlayerOutgoingPacket(int queueId, int gameType, UUID uuid)
	{
		this.queueId = queueId;
		this.gameType = gameType;
		this.uuid = uuid;
	}
	
	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte(QueuePlayerOutgoingPacket.PACKET_ID);
		buf.writeInt(this.queueId);
		buf.writeByte(this.gameType);
		ByteBufUtils.writeUUID(buf, this.uuid);
		return buf;
	}
}
