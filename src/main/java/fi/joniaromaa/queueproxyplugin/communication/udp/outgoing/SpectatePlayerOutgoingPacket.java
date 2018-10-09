package fi.joniaromaa.queueproxyplugin.communication.udp.outgoing;

import java.util.UUID;

import fi.joniaromaa.queueproxyplugin.communication.udp.UdpOutgoingPacket;
import fi.joniaromaa.queueproxyplugin.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SpectatePlayerOutgoingPacket implements UdpOutgoingPacket
{
	private static final int PACKET_ID = 2;
	
	private int queueId;
	private UUID whoUniqueId;
	private UUID targetUniqueId;
	
	public SpectatePlayerOutgoingPacket(int queueId, UUID whoUniqueId, UUID targetUniqueId)
	{
		this.queueId = queueId;
		this.whoUniqueId = whoUniqueId;
		this.targetUniqueId = targetUniqueId;
	}

	@Override
	public ByteBuf getBytes()
	{
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte(SpectatePlayerOutgoingPacket.PACKET_ID);
		buf.writeInt(this.queueId);
		ByteBufUtils.writeUUID(buf, this.whoUniqueId);
		ByteBufUtils.writeUUID(buf, this.targetUniqueId);
		return buf;
	}
}
