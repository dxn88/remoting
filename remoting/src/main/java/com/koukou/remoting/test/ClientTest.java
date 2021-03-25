package com.koukou.remoting.test;

import com.koukou.remoting.CommandCustomHeader;
import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.exception.RemotingCommandException;
import com.koukou.remoting.exception.RemotingConnectException;
import com.koukou.remoting.exception.RemotingSendRequestException;
import com.koukou.remoting.exception.RemotingTimeoutException;
import com.koukou.remoting.netty.NettyClientConfig;
import com.koukou.remoting.netty.NettyRemotingClient;
import com.koukou.remoting.netty.impl.SendMessageRequestHeader;
import com.koukou.remoting.protocol.RemotingCommand;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;

/**
 * @author Dai.xn
 * @date 2021/3/24
 * @since 1.0.0
 */
public class ClientTest {

    public static String IP_PORT = "127.0.0.1:8888";
    public static Integer TIME_OUT = 30000;

    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException, RemotingSendRequestException, RemotingConnectException, RemotingCommandException {

        NettyRemotingClient client = init();

        RemotingCommand requestCommand = constructRequest();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    RemotingCommand responseCommand =  client.invokeSync(IP_PORT, requestCommand, TIME_OUT);
                    callback(responseCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
//        RemotingCommand requestCommand = constructRequest();

//        RemotingCommand responseCommand = client.invokeSync(IP_PORT, requestCommand, TIME_OUT);
//
//        callback(responseCommand);

//        client.shutdown();
    }



    public static NettyRemotingClient init(){
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        NettyRemotingClient nettyRemotingClient = new NettyRemotingClient(nettyClientConfig);

        ArrayList<String > ips = new ArrayList<>();
        ips.add(IP_PORT);
        nettyRemotingClient.updateServerAddressList(ips);
        nettyRemotingClient.start();

        return nettyRemotingClient;
    }

    public static RemotingCommand constructRequest() {
        SendMessageRequestHeader messageRequestHeader = new SendMessageRequestHeader();
        messageRequestHeader.setName("dxn");
        messageRequestHeader.setProductId(9527);
        RemotingCommand requestCommand = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE
                , messageRequestHeader);
        requestCommand.setBody("give me product data".getBytes(CharsetUtil.UTF_8));

        return requestCommand;
    }

    public static void callback(RemotingCommand responseCommand) throws RemotingCommandException {
        CommandCustomHeader commandCustomHeader = responseCommand.decodeCommandCustomHeader(SendMessageRequestHeader.class);

        System.out.println("commandCustomHeader = " + commandCustomHeader);
        byte[] body = responseCommand.getBody();
        if (body != null) {
            String result = new String(body, CharsetUtil.UTF_8);
            System.out.println("result = " + result);
        }

        String remark = responseCommand.getRemark();
        System.out.println("remark = " + remark);
    }

}
