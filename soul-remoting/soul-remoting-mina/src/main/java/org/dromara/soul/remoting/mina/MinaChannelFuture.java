/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.dromara.soul.remoting.mina;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.dromara.soul.remoting.api.Channel;
import org.dromara.soul.remoting.api.ChannelFuture;
import org.dromara.soul.remoting.api.ChannelFutureListener;

/**
 * MinaChannelFuture
 * CreateDate: 2019/10/15 17:23
 *
 * @author sixh
 */
public class MinaChannelFuture implements ChannelFuture {

    private IoFuture future;

    public MinaChannelFuture(IoFuture future) {
        this.future = future;
    }

    @Override
    public Channel channel() {
        return new MinaChannel(future.getSession());
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public Throwable cause() {
        if (future instanceof WriteFuture) {
            WriteFuture wf = (WriteFuture) future;
            return wf.getException();
        }
        return null;
    }

    @Override
    public void addListener(ChannelFutureListener futureListener) {
        future.addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                if (future.isDone()) {
                    futureListener.complete();
                }
            }
        });
    }
}