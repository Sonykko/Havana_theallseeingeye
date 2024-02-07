package org.alexdev.havana.game.commands;

import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;

public class CommandFormatBuilder {

    private static final int SHOCKWAVE = 0;
    private static final int FLASH = 1;

    private StringBuilder stringBuilder;
    private int currentClient;

    private String[] newLine = new String[]{"<br>","\n"};

    public CommandFormatBuilder(Entity caller) {
        if (caller.getType() != EntityType.PLAYER) {
            throw new IllegalArgumentException("Caller needs to be player");
        }

        var player = (Player)caller;

        if(player.flash) {
            currentClient = FLASH;
        } else {
            currentClient = SHOCKWAVE;
        }

        this.stringBuilder = new StringBuilder();
    }

    public CommandFormatBuilder append(Object s) {
        this.stringBuilder.append(s.toString());
        return this;
    }

    public CommandFormatBuilder newLine() {
        this.stringBuilder.append(newLine[currentClient]);
        return this;
    }

    public StringBuilder build() {
        return this.stringBuilder;
    }

    @Override
    public String toString() {
        return build().toString();
    }
}
