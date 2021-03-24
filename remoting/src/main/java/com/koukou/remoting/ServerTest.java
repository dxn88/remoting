package com.koukou.remoting;

import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.netty.NettyRemotingServer;
import com.koukou.remoting.netty.NettyServerConfig;
import com.koukou.remoting.netty.impl.ServerRequestProcessor;

/**
 * @author Dai.xn
 * @date 2021/3/24
 * @since 1.0.0
 */
public class ServerTest {

    public static void main(String[] args) {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        NettyRemotingServer server = new NettyRemotingServer(nettyServerConfig);
        server.registerProcessor(RequestCode.SEND_MESSAGE, new ServerRequestProcessor(), null);
        server.start();
    }

}
