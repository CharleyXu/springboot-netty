package com.xu.springbootnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Test 服务端
 */

public class NettyServer {
	private int port;

	public NettyServer(int port) {
		this.port = port;
	}

	public void run() {
		/***
		 * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
		 * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。
		 * 在这个例子中我们实现了一个服务端的应用，
		 * 因此会有2个NioEventLoopGroup会被使用。
		 * no.1‘boss’，用来接收进来的连接	no.2‘worker’，用来处理已经被接收的连接，
		 * 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
		 * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
		 * 并且可以通过构造函数来配置他们的关系。
		 */
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		System.out.println("准备运行 端口：" + port);

		try {
			//ServerBootstrap 是一个启动NIO服务的辅助启动类,可以在这个服务中直接使用Channel
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			//step1,如果没有设置group将会报java.lang.IllegalStateException: group not set异常
			serverBootstrap.group(bossGroup, workGroup)        //1
					.channel(NioServerSocketChannel.class)        //2
					.childHandler(new WebsocketChatServerInitializer())        //3
					.option(ChannelOption.SO_BACKLOG, 128)    //4
					.childOption(ChannelOption.SO_KEEPALIVE, true);    //5
			//step2,ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接	这里告诉Channel如何获取新的连接.
			//step3,重写ChannelInitializer类，配置一个新的Channel
			//step4,option()是提供给NioServerSocketChannel用来接收进来的连接
			//step5,childOption()是提供给由父管道ServerChannel接收到的连接
			//绑定端口并启动去接收进来的连接
			ChannelFuture future = serverBootstrap.bind(port).sync();
			//一直等待，直到socket被关闭
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//优雅关闭
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			System.out.println("NettyServer 关闭");
		}
	}

	public static void main(String[] args) {
		int port = 8080;
		new NettyServer(port).run();
		//通过cmd窗口的telnet 127.0.0.1 8080运行
	}

}
