package com.koukou.remoting;

import com.koukou.remoting.netty.NettyClientConfig;
import com.koukou.remoting.netty.NettyRemotingClient;

import java.util.ArrayList;

/**
 * @author Dai.xn
 * @date 2021/3/24
 * @since 1.0.0
 */
public class ClientTest {
    public static void main(String[] args) {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyClientConfig);
//        this.remotingClient.registerRPCHook(rpcHook);
//        this.remotingClient.registerProcessor(RequestCode.CHECK_TRANSACTION_STATE, this.clientRemotingProcessor, null);

        ArrayList<String > ips = new ArrayList<>();
        ips.add("172.0.0.1");

        nettyRemotingClient.updateNameServerAddressList(ips);
    }

}
