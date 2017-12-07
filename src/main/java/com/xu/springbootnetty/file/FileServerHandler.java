package com.xu.springbootnetty.file;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {
	private String serverDest;

	public FileServerHandler(String serverDest) {
		this.serverDest = serverDest;
	}

	private static final String CR = System.getProperty("line.separator");

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		File file = new File(serverDest);
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!file.isFile()) {
			ctx.writeAndFlush("Not a file : " + file + CR);
			return;
		}

		System.out.println("msg:   " + msg);
//		Files.write(msg.getBytes(), file);
		Files.append(CR+msg,file, Charsets.UTF_8);
	}
}
