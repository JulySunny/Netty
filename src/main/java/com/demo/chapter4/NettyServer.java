package com.demo.chapter4;

public class NettyServer {

    public static void main(String[] args) {
        new TimeServer().bind(8001);
    }

}


