package org.alexdev.havana.messages.incoming.catalogue;

import org.alexdev.havana.game.catalogue.CatalogueManager;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.catalogue.CATALOGUE_PAGES;
import org.alexdev.havana.messages.types.MessageEvent;
import org.alexdev.havana.server.netty.streams.NettyRequest;

public class GET_CATALOG_INDEX implements MessageEvent {

    private boolean cacheCatalogue;

    public GET_CATALOG_INDEX(boolean cacheCatalogue) {
        this.cacheCatalogue = cacheCatalogue;
    }

    @Override
    public void handle(Player player, NettyRequest reader) {
        player.send(new CATALOGUE_PAGES(
                player,
                player.getDetails().getRank().getRankId(),
                player.getDetails().hasClubSubscription(),
                cacheCatalogue,
                CatalogueManager.getInstance().getChildPages(-1, player.getDetails().getRank().getRankId(), player.getDetails().hasClubSubscription())));
    }
}
