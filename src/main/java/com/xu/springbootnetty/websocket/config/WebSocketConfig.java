package com.xu.springbootnetty.websocket.config;

import com.xu.springbootnetty.websocket.handler.NettyWebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/nettyserver.properties")
public class WebSocketConfig {

  @Value("${tcp.port}")
  private int tcpPort;

  @Value("${boss.thread.nums}")
  private int bossCount;

  @Value("${worker.thread.nums}")
  private int workerCount;

  @Value("${so.keepalive}")
  private boolean keepAlive;

  @Value("${so.backlog}")
  private int backlog;

  @Autowired
  @Qualifier("nettyWebSocketChannelInitializer")
  private NettyWebSocketChannelInitializer nettyWebSocketChannelInitializer;


  @Bean(name = "serverBootstrap")
  public ServerBootstrap bootstrap() {
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup(), workerGroup())
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .childHandler(nettyWebSocketChannelInitializer);
    Map<ChannelOption, Object> tcpChannelOptions = tcpChannelOptions();
    tcpChannelOptions.forEach((k, v) -> {
      b.option(k, v);
    });
    return b;
  }

  @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
  public NioEventLoopGroup bossGroup() {
    return new NioEventLoopGroup();
  }

  @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
  public NioEventLoopGroup workerGroup() {
    return new NioEventLoopGroup();
  }

  @Bean(name = "tcpChannelOptions")
  public Map<ChannelOption, Object> tcpChannelOptions() {
    Map<ChannelOption, Object> options = new HashMap<>();
    options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
    options.put(ChannelOption.SO_BACKLOG, backlog);
    return options;
  }

  @Bean(name = "tcpSocketAddress")
  public InetSocketAddress tcpPort() {
    return new InetSocketAddress(tcpPort);
  }


}
