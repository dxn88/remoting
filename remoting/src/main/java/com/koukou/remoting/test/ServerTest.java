package com.koukou.remoting.test;

import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.netty.NettyRemotingServer;
import com.koukou.remoting.netty.NettyServerConfig;
import com.koukou.remoting.netty.impl.ServerRequestProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dai.xn
 * @date 2021/3/24
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class ServerTest {

    public static AtomicInteger integer = new AtomicInteger(0);

    public static ExecutorService executorService = Executors.newFixedThreadPool(20, r -> {
        Thread thread = new Thread(r);
        thread.setName("ServerTest#executorService_" + integer.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    });

    public static void main(String[] args) {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        NettyRemotingServer server = new NettyRemotingServer(nettyServerConfig);
        server.registerProcessor(RequestCode.SEND_MESSAGE, new ServerRequestProcessor(), executorService);
        server.start();
    }

}
