package fi.joniaromaa.queueproxyplugin.communication.udp;

import java.util.HashMap;

import fi.joniaromaa.queueproxyplugin.communication.udp.incoming.NoServersFoundIncomingPacket;
import fi.joniaromaa.queueproxyplugin.communication.udp.incoming.SendPlayerIncomingPacket;
import io.netty.channel.socket.DatagramPacket;

public class UdpPacketManager
{
	private HashMap<Integer, UdpIncomingPacket> incomingPackets = new HashMap<>();
	
	public UdpPacketManager()
	{
		this.incomingPackets.put(0, new SendPlayerIncomingPacket());
		this.incomingPackets.put(1, new NoServersFoundIncomingPacket());
	}
	
	public void handleIncoming(DatagramPacket msg)
	{
		int packetId = msg.content().readByte();
		
		UdpIncomingPacket incoming = this.incomingPackets.get(packetId);
		if (incoming != null)
		{
			incoming.handle(msg);
		}
		else
		{
			System.out.println("Unhandled packet: " + packetId);
		}
	}
}
