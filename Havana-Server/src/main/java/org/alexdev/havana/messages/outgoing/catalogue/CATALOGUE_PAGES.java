package org.alexdev.havana.messages.outgoing.catalogue;

import org.alexdev.havana.game.catalogue.CatalogueManager;
import org.alexdev.havana.game.catalogue.CataloguePage;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.types.MessageComposer;
import org.alexdev.havana.server.netty.streams.NettyResponse;

import java.util.List;

public class CATALOGUE_PAGES extends MessageComposer {
    private final Player player;
    private final int rank;
    private final boolean hasClub;
    private final List<CataloguePage> parentTabs;
    private static List<CataloguePage> cache;

    private boolean cacheCatalogue;

    public CATALOGUE_PAGES(Player player, int rank, boolean hasClub, boolean cacheCatalogue, List<CataloguePage> parentTabs) {
        this.player = player;
        this.rank = rank;
        this.hasClub = hasClub;
        this.parentTabs = parentTabs;
        this.cacheCatalogue = cacheCatalogue;
    }

    @Override
    public void compose(NettyResponse response) {
        if(!this.player.getHasLoadedCatalogue()) {
            response.writeInt(0); // Navigatable
            response.writeInt(0); // Colour
            response.writeInt(0); // Icon
            response.writeInt(-1); // Page id
            response.writeString("");
            response.writeInt(0);
        }

        response.writeInt(this.parentTabs.size());

        for (CataloguePage childTab : this.parentTabs) {
            appendIndexNode(childTab, response);
            recursiveIndexNode(childTab, response);
        }

        if(this.cacheCatalogue && this.player.flash) {
            this.player.setHasLoadedCatalogue(true);
            this.player.getLogger().info("Player " + this.player.getDetails().getName() + " just loaded the catalogue");
        }
    }

    public void appendIndexNode(CataloguePage cataloguePage, NettyResponse response) {
        response.writeBool(true); // Navigatable
        response.writeInt(cataloguePage.getColour()); // Colour
        response.writeInt(cataloguePage.getIcon()); // Icon
        response.writeInt(cataloguePage.getId()); // Page id
        response.writeString(cataloguePage.getName());
        response.writeInt(0);
    }

    private void recursiveIndexNode(CataloguePage parentTab, NettyResponse response) {
        List<CataloguePage> childTabs = CatalogueManager.getInstance().getChildPages(parentTab.getId(), this.rank, this.hasClub);
        response.writeInt(childTabs.size());

        for (CataloguePage childTab : childTabs) {
            appendIndexNode(childTab, response);
            recursiveIndexNode(childTab, response);
        }
    }

    @Override
    public short getHeader() {
        return 126; // "A~"
    }
}
