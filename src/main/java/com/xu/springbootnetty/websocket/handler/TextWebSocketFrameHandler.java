package com.xu.springbootnetty.websocket.handler;

import com.xu.springbootnetty.websocket.util.RandomName;
import com.xu.springbootnetty.websocket.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by xsw on 2017/10/12.
 */
@Component
@Qualifier("textWebSocketFrameHandler")
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Autowired
  private RedisUtil redisUtil;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
      TextWebSocketFrame msg) throws Exception {
    Channel incoming = ctx.channel();
    String uName = redisUtil.get(incoming.id() + "");
    for (Channel channel : channels) {
      if (channel != incoming) {
        channel.writeAndFlush(new TextWebSocketFrame("[" + uName + "]" + msg.text()));
      } else {
        channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text()));
      }
    }
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    System.out.println(ctx.channel().remoteAddress());
    String uName = new RandomName().getRandomName();

    Channel incoming = ctx.channel();
    for (Channel channel : channels) {
      channel.writeAndFlush(new TextWebSocketFrame("[新用户] - " + uName + " 加入"));
    }
    redisUtil.set(incoming.id() + "", uName);
    channels.add(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    String uName = redisUtil.get(incoming.id() + "");
    for (Channel channel : channels) {
      channel.writeAndFlush(new TextWebSocketFrame("[用户] - " + uName + " 离开"));
    }
    redisUtil.remove(incoming.id() + "");
    //redisDao.saveString("cacheName",redisDao.getString("cacheName").replaceAll(uName,""));

    channels.remove(ctx.channel());
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("用户:" + redisUtil.get(incoming.id() + "") + "在线");
  }


  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("用户:" + redisUtil.get(incoming.id() + "") + "掉线");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
      throws Exception {
    Channel incoming = ctx.channel();
    System.out.println("用户:" + redisUtil.get(incoming.id() + "") + "异常");
    cause.printStackTrace();
    ctx.close();
  }

}