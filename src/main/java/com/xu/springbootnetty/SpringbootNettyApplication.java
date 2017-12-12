package com.xu.springbootnetty;

import com.xu.springbootnetty.websocket.config.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootNettyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication
				.run(SpringbootNettyApplication.class, args);
		TcpServer tcpServer = context.getBean(TcpServer.class);
		try {
			tcpServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
