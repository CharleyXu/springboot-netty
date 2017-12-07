package com.xu.springbootnetty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 	Echo 应答协议	主要用于调试和检测中
 * 	服务器端
 *
 */
public class EchoServer {
	public void run(int port) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();	// 引导辅助程序
		EventLoopGroup bossGroup = new NioEventLoopGroup();        //NIO来接收连接和处理连接
		EventLoopGroup workGroup = new NioEventLoopGroup();
		System.out.println("文件服务器端准备运行 端口：" + port);

		try {
			//step1,如果没有设置group将会报java.lang.IllegalStateException: group not set异常
			serverBootstrap.group(bossGroup, workGroup)        //1
					.channel(NioServerSocketChannel.class)        //2
					.childHandler(new ChannelInitializer<SocketChannel>() {		//3
						public void initChannel(SocketChannel ch) {
							ch.pipeline().addLast("echoServerHandler",new EchoServerHandler());
						}
					})        //3
					.option(ChannelOption.SO_BACKLOG, 128)    //4
					.childOption(ChannelOption.SO_KEEPALIVE, true);    //5
			//step2,ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接	这里告诉Channel如何获取新的连接.
			//step3,重写ChannelInitializer类，配置一个新的Channel
			//step4,option()是提供给NioServerSocketChannel用来接收进来的连接
			//step5,childOption()是提供给由父管道ServerChannel接收到的连接

			// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
			ChannelFuture future = serverBootstrap.bind(port).sync();

			System.out.println(EchoServer.class.getName() + " started and listen on " +future.channel().localAddress());
			//一直等待，直到channel关闭
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//优雅关闭	释放掉所有资源包括创建的线程
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			System.out.println("EchoServer 关闭");
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new EchoServer().run(port);
	}
}
