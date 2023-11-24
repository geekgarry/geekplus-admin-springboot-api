package com.geekplus.framework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 10/13/23 21:22
 * description: 做什么的？
 */
public class NettySocketServer {
    /**
     * 单例静态内部类
     */
    public static class SingletionWSServer {
        static final NettySocketServer instance = new NettySocketServer();
    }

    public static NettySocketServer getInstance() {
        return SingletionWSServer.instance;
    }

    private NioEventLoopGroup mainGroup;
    private NioEventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public NettySocketServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SocketServerInitialzer()); // 添加自定义初始化处理器
    }

    public void start() {
        //端口号不要和spring的端口一样
        future = this.server.bind(8086);
        System.err.println("netty 服务端启动完毕 .....");
    }
}
