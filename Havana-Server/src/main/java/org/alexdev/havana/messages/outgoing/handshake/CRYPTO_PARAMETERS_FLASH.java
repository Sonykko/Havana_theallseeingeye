/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.handshake;

import org.alexdev.havana.game.encryption.DiffieHellman;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class CRYPTO_PARAMETERS_FLASH extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        //response.writeString(DiffieHellman.generateRandomNumString(32));
        //response.writeInt(0);
        response.writeInt(9);
        response.writeInt(0);
        response.writeInt(0);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(3);
        response.writeInt(0);
        response.writeInt(2);
        response.writeInt(1);
        response.writeInt(4);
        response.writeInt(1);
        response.writeInt(5);
        response.writeString("dd-MM-yyyy");
        response.writeInt(7);
        response.writeBool(false);
        response.writeInt(8);
        response.writeString("/client");
        response.writeInt(9);
        response.writeBool(false);
    }

    @Override
    public short getHeader() {
        return 257;
    }
}
