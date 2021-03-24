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
import com.koukou.remoting.common.vo.RequestCode;
import com.koukou.remoting.exception.RemotingCommandException;
import com.koukou.remoting.netty.AsyncNettyRequestProcessor;
import com.koukou.remoting.netty.NettyRequestProcessor;
import com.koukou.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("all")
public class DefaultRequestProcessor extends AsyncNettyRequestProcessor implements NettyRequestProcessor {


    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx,
                                          RemotingCommand request) throws RemotingCommandException {

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
        return null;
    }

    private RemotingCommand processSendMessage(ChannelHandlerContext ctx, RemotingCommand request) {
        return null;
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }


}
