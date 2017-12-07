package com.xu.springbootnetty.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 文件服务端处理器
 */

/**
 * Sharable表示此对象在channel间共享 handler类是我们的具体业务类
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	//写回数据
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("---channelRead---");
		System.out.println("server received data : " + msg);
		ctx.writeAndFlush(msg);
	}
	//写回数据完成
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("---channelReadComplete---");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)	//flush掉所有写回的数据
				.addListener(ChannelFutureListener.CLOSE);	//当flush完成后关闭channel
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("---exceptionCaught---");
		cause.printStackTrace();//捕捉异常信息
		ctx.close();//出现异常时关闭channel
	}

	//	private static final String CR = System.getProperty("line.separator");
//
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, String msg)
//			throws Exception {
//		File file = new File(msg);
//		if (file.exists()) {
//			if (!file.isFile()) {
//				ctx.writeAndFlush("Not a file : " + file + CR);
//				return;
//			}
//			ctx.write(file + " " + file.length() + CR);
//			RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
//			FileRegion region = new DefaultFileRegion(
//					randomAccessFile.getChannel(), 0, randomAccessFile.length());
//			ctx.write(region);
//			ctx.writeAndFlush(CR);
//			randomAccessFile.close();
//		} else {
//			ctx.writeAndFlush("File not found: " + file + CR);
//		}
//
//	}
}