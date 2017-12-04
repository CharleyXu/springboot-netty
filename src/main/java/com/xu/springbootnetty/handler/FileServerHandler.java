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
public class FileServerHandler  extends SimpleChannelInboundHandler<String> {
	private static final String CR = System.getProperty("line.separator");//换行符
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
		File file = new File(s);
		if (file.exists()) {
			if (!file.isFile()) {
				channelHandlerContext.writeAndFlush("Not a file : " + file + CR);
				return;
			}
			channelHandlerContext.write(file + " " + file.length() + CR);
			RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r");
			FileRegion region = new DefaultFileRegion(
					randomAccessFile.getChannel(), 0, randomAccessFile.length());
			channelHandlerContext.write(region);
			channelHandlerContext.writeAndFlush(CR);
			randomAccessFile.close();
		} else {
			channelHandlerContext.writeAndFlush("File not found: " + file + CR);
		}
	}
}
