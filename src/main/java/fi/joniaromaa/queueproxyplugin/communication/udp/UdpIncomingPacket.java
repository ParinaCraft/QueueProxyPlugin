package fi.joniaromaa.queueproxyplugin.communication.udp;

import io.netty.channel.socket.DatagramPacket;

public interface UdpIncomingPacket
{
	public void handle(DatagramPacket msg);
}
