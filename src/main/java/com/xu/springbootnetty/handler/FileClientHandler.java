package com.xu.springbootnetty.handler;

import java.io.File;
import java.io.FileOutputStream;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 文件客户端处理器
 */
public class FileClientHandler extends SimpleChannelInboundHandler<String> {
	private String dest;

	/**
	 *
	 * @param dest 文件生成路径
	 */
	public FileClientHandler(String dest) {
		this.dest = dest;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
		File file = new File(dest);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(o.getBytes());
		fos.close();
	}
}
