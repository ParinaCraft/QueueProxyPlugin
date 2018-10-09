package fi.joniaromaa.queueproxyplugin.utils;

import java.util.UUID;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;

public class ByteBufUtils
{
	public static void writeUUID(ByteBuf buf, UUID uuid)
	{
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}
	
	public static UUID readUUID(ByteBuf buf)
	{
		return new UUID(buf.readLong(), buf.readLong());
	}
	public static void writeBytes(ByteBuf buffer, byte[] bytes)
	{
		buffer.writeShort(bytes.length);
		buffer.writeBytes(bytes);
	}
	
	public static void writeString(ByteBuf buffer, String string)
	{
		ByteBufUtils.writeBytes(buffer, string.getBytes(Charsets.UTF_8));
	}
	
	public static byte[] readBytes(ByteBuf buffer)
	{
		int length = buffer.readUnsignedShort();
		
		byte[] bytes = new byte[length];
		buffer.readBytes(bytes);
		
		return bytes;
	}
	
	public static String readString(ByteBuf buffer)
	{
		return new String(ByteBufUtils.readBytes(buffer), Charsets.UTF_8);
	}
}
