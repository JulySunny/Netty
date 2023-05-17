package com.demo.chapter3;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class NettyThreadPoolHandler extends ChannelInboundHandlerAdapter {


    private AtomicInteger atomicInteger = new AtomicInteger();


    public NettyThreadPoolHandler() {
        System.out.println("NettyThreadPoolHandler 实例化成功");
    }

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket datagramPacket = (DatagramPacket) msg;
        System.out.println("进入NettyThreadPoolHandler");
        System.out.println("消息内容:"+datagramPacket.toString());
        executor.execute(() -> {
            System.out.println("这是Netty ThreadPoolChannelHandler中执行的异步操作");
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        atomicInteger.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        atomicInteger.decrementAndGet();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("TcpCountHandler exceptionCaught");
        cause.printStackTrace();
    }
}
