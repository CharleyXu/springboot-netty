package com.xu.springbootnetty.websocket.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("nettyWebSocketChannelInitializer")
public class NettyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

  @Autowired
  private TextWebSocketFrameHandler textWebSocketFrameHandler;

  @Override
  public void initChannel(SocketChannel ch) throws Exception {//2
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new HttpServerCodec());
    pipeline.addLast(new HttpObjectAggregator(64 * 1024));
    pipeline.addLast(new ChunkedWriteHandler());
    pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
    pipeline.addLast(textWebSocketFrameHandler);
  }
}