package com.xu.springbootnetty.file;

import com.google.common.base.Charsets;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileClientHandler extends SimpleChannelInboundHandler<String> {

  private String dest;

  /**
   * @param dest 文件生成路径
   */
  public FileClientHandler(String dest) {
    this.dest = dest;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg)
      throws Exception {

    File file = new File(dest);
    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IOException("can not create file by io exception");
      }
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      fos.write(msg.getBytes(Charsets.UTF_8));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (fos != null) {
        fos.close();
      }
    }
  }

}
