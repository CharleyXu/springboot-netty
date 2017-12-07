package com.xu.springbootnetty.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class FileServer {
	private int port;
	private String dest; // 接收到文件存放的路径

	public FileServer(int port, String dest) {
		this.port = port;
		this.dest = dest;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(
									new StringEncoder(CharsetUtil.UTF_8),
									new LineBasedFrameDecoder(1024),
									new StringDecoder(CharsetUtil.UTF_8),
									new FileServerHandler(dest));
						}
					});
			ChannelFuture f = b.bind(port).sync();
			System.out.println("Server start at port : " + port);
			f.channel().closeFuture().sync();
		} finally {
			// 优雅停机
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		String dest = "D:/sever.txt";
		new FileServer(port,dest).run();
	}
}
