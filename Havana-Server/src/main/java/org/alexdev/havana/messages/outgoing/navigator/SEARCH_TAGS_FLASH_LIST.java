/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.messages.outgoing.navigator;

import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

public class SEARCH_TAGS_FLASH_LIST extends MessageComposer {

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(0);

        /*foreach (KeyValuePair<string, int> TagData in SortedTags)
        {
            Message.AppendStringWithBreak(TagData.Key);
            Message.AppendInt32(TagData.Value);
        }*/
    }

    @Override
    public short getHeader() {
        return 452;
    }
}
