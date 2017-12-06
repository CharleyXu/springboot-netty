package com.xu.springbootnetty.handler;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 文件服务端处理器
 */
public class FileServerHandler extends SimpleChannelInboundHandler<String> {

	private static final String CR = System.getProperty("line.separator");

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		File file = new File(msg);
		if (file.exists()) {
			if (!file.isFile()) {
				ctx.writeAndFlush("Not a file : " + file + CR);
				return;
			}
			ctx.write(file + " " + file.length() + CR);
			RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
			FileRegion region = new DefaultFileRegion(
					randomAccessFile.getChannel(), 0, randomAccessFile.length());
			ctx.write(region);
			ctx.writeAndFlush(CR);
			randomAccessFile.close();
		} else {
			ctx.writeAndFlush("File not found: " + file + CR);
		}

	}
}