package com.demo.chapter4;

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

/**
 * @author Sam.yang
 * @since 2023/5/15 01:26
 */
public class TimeServer {

    public void bind(int port) {

        //配置服务端的NIO 线程组 实际上它们就是Reactor线程组
        //创建两个的原因是:
        //一个用于服务端接受客户端的连接
        //另一个用于进行SocketChannel的网络读写
        EventLoopGroup bossGroups = new NioEventLoopGroup();
        EventLoopGroup workGroups = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroups, workGroups)
                    .channel(NioServerSocketChannel.class)
                    /**
                     TCP backlog是指TCP服务器在处理客户端连接请求时，可以排队等待的最大连接数。当服务器的连接请求队列已满时，新的连接请求将被拒绝。TCP backlog的大小可以通过操作系统的参数进行配置，通常默认值为128。
                     当服务器的并发连接数较高时，TCP backlog的大小需要适当调整，以避免连接请求被拒绝。如果TCP backlog设置过小，可能会导致连接请求被拒绝，从而影响服务器的可用性。
                     如果TCP backlog设置过大，可能会占用过多的系统资源，从而影响服务器的性能。
                     在Netty中，可以通过ServerBootstrap的option方法和childOption方法设置TCP backlog的大小。例如：
                     ServerBootstrap的option方法设置了TCP backlog的大小为1024。这意味着服务器可以排队等待1024个连接请求。如果连接请求队列已满，新的连接请求将被拒绝。
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    /**
                     *  绑定IO处理事件childHandler 主要用于处理网络I/O事件 例如记录日志,对消息进行编码等等
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //Netty提供了多种编码器和解码器
                            //解决粘包的问题
                            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            channel.pipeline().addLast(new StringDecoder());
                            channel.pipeline().addLast(new TimeServerHandler3());
                        }
                    })
            ;
            //绑定端口 ,同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();


            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println("异常" + e);
        } finally {
            //优雅推出 释放线程池资源
            bossGroups.shutdownGracefully();
            workGroups.shutdownGracefully();
        }
    }

}
