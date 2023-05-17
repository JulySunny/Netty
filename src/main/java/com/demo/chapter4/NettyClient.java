package com.demo.chapter4;


public class NettyClient {

    public static void main(String[] args) {
        new TimeClient().bind(8001);
    }

//    public void run(int beginPort) {

//        System.out.println("客户端启动中");
//
//        EventLoopGroup group = new NioEventLoopGroup();
//
//        Bootstrap bootstrap = new Bootstrap();
//
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_REUSEADDR, true)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//
//                    }
//
//                });
//        bootstrap.connect(SERVER, Config.BEGIN_PORT).addListener(future ->
//                {
//                    if (future.isSuccess()) {
//                        System.out.println("链接创建成功");
//                    }
//
//                }
//        );

//        int index = 0 ;
//        int finalPort ;
//        while (true){
//
//            finalPort = beginPort + index;
//
//            try {
//                bootstrap.connect(SERVER, finalPort).addListener((ChannelFutureListener)future ->{
//                    if(!future.isSuccess()){
//                        System.out.println("创建连接失败 " );
//                    }
//                }).get();
//
//            } catch (Exception e) {
//                //e.printStackTrace();
//            }
//            ++index;
//            if(index == (endPort - beginPort)){
//                index = 0 ;
//            }

    }


//    }


//}
