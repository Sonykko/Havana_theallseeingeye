/**
 * Author: Jeppe#9821 -- github.com/JeppsonDev
 */

package org.alexdev.havana.game.inventory;

import org.alexdev.havana.game.item.Item;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.messages.outgoing.inventory.INVENTORY;
import org.alexdev.havana.util.StringUtil;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlashInventory extends Inventory {

    public FlashInventory(Player player, int maxItems) {
        super(player, maxItems);
    }

    @Override
    protected void refreshPagination() {
        this.displayedItems = new CopyOnWriteArrayList<>();
        int orderId = 0;

        for (Item item : this.items) {
            this.displayedItems.add(item);
        }

        this.displayedItems.sort(Comparator.comparingInt(Item::getOrderId));
        this.paginatedItems = new ConcurrentHashMap<>(StringUtil.paginate(this.displayedItems, MAX_ITEMS_PER_PAGE));
    }

    @Override
    public void getView(String stripView) {
        this.refreshPagination();

        Map<Integer, Item> casts = this.getCasts();
        this.player.send(new INVENTORY(this, casts));
    }
}
