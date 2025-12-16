package org.alexdev.havana.messages.flash.outgoing.modtool;

import org.alexdev.havana.dao.mysql.CFHTopicsDao;
import org.alexdev.havana.game.moderation.cfh.topics.CFHTopics;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.List;

public class FLASH_MODTOOL extends MessageComposer {
    private List<CFHTopics> userMessagePresets;
    private List<CFHTopics> roomMessagePresets;

    public FLASH_MODTOOL() {
        // TODO: Remove this yucky hardcoded bullshit
        // Fixed: Now using actual topics from CFHTopicsDao. ;) -Sonykko
        this.userMessagePresets = CFHTopicsDao.getCFHTopics();
        this.roomMessagePresets = CFHTopicsDao.getCFHTopics();
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(-1);
        response.writeInt(this.userMessagePresets.size());

        for (CFHTopics preset : this.userMessagePresets) {
            String topic = preset.getSanctionReasonValue() + ". " + (preset.getSanctionReasonDesc() != null ? preset.getSanctionReasonDesc() : "");
            response.writeString(topic);
        }

        response.writeInt(0);
        response.writeInt(14);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);

        response.writeInt(this.roomMessagePresets.size());

        for (CFHTopics preset : this.roomMessagePresets) {
            String topic = preset.getSanctionReasonValue() + ". " + (preset.getSanctionReasonDesc() != null ? preset.getSanctionReasonDesc() : "");
            response.writeString(topic);
        }

        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeString("test");
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeString("test");
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeInt(1);
        response.writeString("test");
    }

    @Override
    public short getHeader() {
        return 531;
    }
}
