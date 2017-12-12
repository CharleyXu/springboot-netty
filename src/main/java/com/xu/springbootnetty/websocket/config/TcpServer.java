package com.xu.springbootnetty.websocket.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpServer {

  @Autowired
  private ServerBootstrap serverBootstrap;

  @Autowired
  private InetSocketAddress tcpPort;

  private Channel serverChannel;

  @PreDestroy
  public void stop() throws Exception {
    serverChannel.close();
    serverChannel.parent().close();
  }

  public void start() throws Exception {
    serverChannel = serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
  }

}
