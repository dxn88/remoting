/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.koukou.remoting.netty.impl;

import com.koukou.remoting.common.RemotingHelper;
import com.koukou.remoting.common.vo.MessageConst;
import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.common.vo.ResponseCode;
import com.koukou.remoting.exception.RemotingCommandException;
import com.koukou.remoting.netty.AsyncNettyRequestProcessor;
import com.koukou.remoting.netty.NettyRequestProcessor;
import com.koukou.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


@Slf4j
@SuppressWarnings("all")
public class ServerRequestProcessor extends AsyncNettyRequestProcessor implements NettyRequestProcessor {

    private Semaphore sendMessageFlow = new Semaphore(10, true);

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx,
                                          RemotingCommand request) throws RemotingCommandException, InterruptedException {

        if (ctx != null) {
            log.debug("receive request, {} {} {}",
                    request.getCode(),
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel()),
                    request);
        }

        switch (request.getCode()) {
            case RequestCode.SEND_MESSAGE:
                return processSendMessage(ctx, request);
            case RequestCode.PUT_KV:
                return doPutKVConfig(ctx, request);
            default:
                break;
        }
        return null;
    }

    private RemotingCommand doPutKVConfig(ChannelHandlerContext ctx, RemotingCommand request) {
        System.out.println("DefaultRequestProcessor.doPutKVConfig");
        return null;
    }

    private RemotingCommand processSendMessage(ChannelHandlerContext ctx, RemotingCommand request) throws InterruptedException {
        boolean acquired = this.sendMessageFlow.tryAcquire(3000, TimeUnit.MILLISECONDS);
        if (acquired) {
            try {
                HashMap<String, String> extFields = request.getExtFields();
                System.out.println("extFields = " + extFields);

                final RemotingCommand response = RemotingCommand.createResponseCommand(null);
                response.setOpaque(request.getOpaque());
                response.addExtField(MessageConst.NAME, "server:" + extFields.get(MessageConst.NAME));
                response.addExtField(MessageConst.PRODUCT_ID, extFields.get(MessageConst.PRODUCT_ID) + "0");

                byte[] bytes = doProcessSendMessage(request);

                response.setBody(bytes);
                response.setCode(ResponseCode.SUCCESS);
                response.setRemark(null);
                return response;
            } finally {
                sendMessageFlow.release();
            }
        }

        return null;
    }

    private byte[] doProcessSendMessage(RemotingCommand request) {

        byte[] body = request.getBody();
        String result = new String(body, CharsetUtil.UTF_8);
        System.out.println("result = " + result);

        return "return your data from server".getBytes();
    }

    @Override
    public boolean rejectRequest() {
        return sendMessageFlow.availablePermits() <= 0;
    }


}
