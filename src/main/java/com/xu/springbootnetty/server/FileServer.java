package com.xu.springbootnetty.server;

import com.xu.springbootnetty.handler.FileServerHandler;

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

/**
 * 文件服务器端
 */
public class FileServer {
	public void run(int port){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		System.out.println("文件服务器端准备运行 端口：" + port);

		try {

			ServerBootstrap serverBootstrap = new ServerBootstrap();
			//step1,如果没有设置group将会报java.lang.IllegalStateException: group not set异常
			serverBootstrap.group(bossGroup, workGroup)        //1
					.channel(NioServerSocketChannel.class)        //2
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/*
						 * (non-Javadoc)
						 *
						 * @see
						 * io.netty.channel.ChannelInitializer#initChannel(io
						 * .netty.channel.Channel)
						 */
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(
									new StringEncoder(CharsetUtil.UTF_8),
									new LineBasedFrameDecoder(1024),
									new StringDecoder(CharsetUtil.UTF_8),
									new FileServerHandler());
						}
					})        //3
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
	public static void main(String[] args) throws Exception {
		int port = 8081;
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new FileServer().run(port);
	}
}
