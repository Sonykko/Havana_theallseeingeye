/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.server.netty;

import org.alexdev.havana.messages.MessageHandler;
import org.alexdev.havana.messages.MessageHandlerFlash;
import org.alexdev.havana.server.netty.codec.NetworkDecoder;
import org.alexdev.havana.server.netty.codec.NetworkEncoder;
import org.alexdev.havana.server.netty.codec.NetworkDecoderFlash;
import org.alexdev.havana.server.netty.connections.ConnectionHandler;

public class NettyChannelInitializerFlash extends NettyChannelInitializer {
    public NettyChannelInitializerFlash(NettyServer nettyServer) {
        super(nettyServer);
    }

    @Override
    protected NetworkDecoder getNetworkDecoder() {
        return new NetworkDecoderFlash();
    }

    @Override
    protected ConnectionHandler getConnectionHandler() {
        return new ConnectionHandler(this.nettyServer, new MessageHandlerFlash());
    }
}
