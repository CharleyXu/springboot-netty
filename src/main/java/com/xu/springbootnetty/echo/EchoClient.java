package com.xu.springbootnetty.echo;

import java.io.IOException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Echo客户端
 */
public class EchoClient {
	private String host;
	private int port;

	/**
	 *
	 */
	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() throws InterruptedException, IOException {
		Bootstrap b = new Bootstrap(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});

			// 启动客户端
			ChannelFuture f = b.connect(host, port).sync(); // (5)
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					if (channelFuture.isSuccess()){
						System.out.println("client connected");
					}else {
						System.out.println("server attemp failed");
						channelFuture.cause().printStackTrace();
					}
				}
			});
			// 等待连接关闭
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		new EchoClient("localhost", 8080).run();
	}
}
