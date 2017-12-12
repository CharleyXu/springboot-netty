package com.xu.springbootnetty.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NettyController {

  @RequestMapping("/")
  public String index() {
    return "/SocketChatClient";
  }
}
