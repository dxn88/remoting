package com.koukou.remoting;

import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.exception.RemotingConnectException;
import com.koukou.remoting.exception.RemotingSendRequestException;
import com.koukou.remoting.exception.RemotingTimeoutException;
import com.koukou.remoting.netty.NettyClientConfig;
import com.koukou.remoting.netty.NettyRemotingClient;
import com.koukou.remoting.netty.impl.SendMessageRequestHeader;
import com.koukou.remoting.netty.impl.ServerRequestProcessor;
import com.koukou.remoting.protocol.RemotingCommand;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;

/**
 * @author Dai.xn
 * @date 2021/3/24
 * @since 1.0.0
 */
public class ClientTest {

    public static String IP_PORT = "172.0.0.1:8888";
    public static Integer TIME_OUT = 3000;

    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException, RemotingConnectException {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyClientConfig);

        ArrayList<String > ips = new ArrayList<>();
        ips.add(IP_PORT);
        nettyRemotingClient.updateNameServerAddressList(ips);
        nettyRemotingClient.start();

        SendMessageRequestHeader messageRequestHeader = new SendMessageRequestHeader();
        messageRequestHeader.setName("dxn");
        messageRequestHeader.setProductId(9527);
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE
                , messageRequestHeader);
        requestCommand.setBody("give me product data".getBytes(CharsetUtil.UTF_8));
        RemotingCommand responseCommand = nettyRemotingClient.invokeSync(IP_PORT, requestCommand, TIME_OUT);
        byte[] body = responseCommand.getBody();
        String result = new String(body, CharsetUtil.UTF_8);
        System.out.println("result = " + result);

        nettyRemotingClient.shutdown();
    }

}
