package fi.joniaromaa.queueproxyplugin.communication.udp;

import io.netty.buffer.ByteBuf;

public interface UdpOutgoingPacket
{
	public ByteBuf getBytes();
}
