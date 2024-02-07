package org.alexdev.havana.dao.mysql;

public class RareFurniture {
    public int definitionId;
    public String sprite;
    public int spriteId;

    public RareFurniture(int definitionId, String sprite, int spriteId) {
        this.definitionId = definitionId;
        this.sprite = sprite;
        this.spriteId = spriteId;
    }
}
