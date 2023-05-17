package com.demo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author Sam.yang
 * @since 2023/5/15 01:16
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        ByteBuf buf = (ByteBuf) msg;
        //获取缓冲区可读的字节数,根据刻度的字节数创建byte数组
        byte[] req = new byte[buf.readableBytes()];
        //通过readByte将缓冲区的字节数组复制到新数组中
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("时间服务器收到指令:" + body);
        //判断消息内容
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        //将消息发送队列中的消息写入到SocketChannel中发送给对方
        //这里为什么要用flush:
        //从性能角度考虑，为了防止频繁地唤醒Selector进行消息发送,Netty的write方法并不直接将消息写道SocketChannel中
        //调用write方法只是把待发送的消息放到发送缓冲数组中,再通过调用flush方法,将发送缓存区中的消息全部写道SocketChannel中
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        //当放生异常时,关闭ChannelHandlerContext，释放和ChannelHandlerContext相关联的句柄资源
        System.out.println("发生了异常" + cause);
        ctx.close();
    }
}
