/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class NetworkDecoderFlash extends NetworkDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if(buffer.getByte(0) == 60) {
            ctx.writeAndFlush(getXmlPolicy());
        } else {
            super.decode(ctx, buffer, out);
        }
    }

    private static String getXmlPolicy() {
        return "<?xml version=\"1.0\"?>\r\n<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n<cross-domain-policy>\r\n   <allow-access-from domain=\"*\" to-ports=\"1-65535\" />\r\n</cross-domain-policy>\0";
    }
}
