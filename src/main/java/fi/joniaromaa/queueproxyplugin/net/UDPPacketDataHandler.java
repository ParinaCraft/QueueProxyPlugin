package fi.joniaromaa.queueproxyplugin.net;

import fi.joniaromaa.queueproxyplugin.QueueProxyPlugin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UDPPacketDataHandler extends SimpleChannelInboundHandler<DatagramPacket> 
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception
	{
		QueueProxyPlugin.getPlugin().getNetworkManager().getUdpPacketManager().handleIncoming(msg);
	}
}
