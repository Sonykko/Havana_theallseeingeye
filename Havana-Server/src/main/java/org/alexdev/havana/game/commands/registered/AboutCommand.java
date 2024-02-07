package org.alexdev.havana.game.commands.registered;

import org.alexdev.havana.game.commands.Command;
import org.alexdev.havana.game.commands.CommandFormatBuilder;
import org.alexdev.havana.game.entity.Entity;
import org.alexdev.havana.game.entity.EntityType;
import org.alexdev.havana.game.player.Player;
import org.alexdev.havana.game.player.PlayerRank;
import org.alexdev.havana.messages.outgoing.alerts.ALERT;

public class AboutCommand extends Command {
    @Override
    public void setPlayerRank() {
        super.setPlayerRank(PlayerRank.NORMAL);
    }

    @Override
    public void handleCommand(Entity entity, String message, String[] args) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player)entity;

        var about = new CommandFormatBuilder(entity);
        about.append("Project Havana - Habbo Hotel v31 emulation").newLine().newLine();
        about.append("Release: r31_20090312_0433_13751_b40895fb6101dbe96dc7b9d6477eeeb4").newLine().newLine();
        about.append("Contributors:").newLine();
        about.append(" - ThuGie, Copyright, Raptosaur, Hoshiko, TOMYSSHADOW, Elijah").newLine();
        about.append("   Romuald, Glaceon, Nillus, Holo Team, Meth0d, office.boy, bbadzz").newLine().newLine();
        about.append("   Big thanks to Sefhriloff & Ascii for assisting with SnowStorm.").newLine().newLine().newLine();
        about.append("   Thanks to Jeppe and Parsnip for CH-feature restoration eg R39 flash client and more").newLine().newLine().newLine();
        about.append("Made by Quackster from RaGEZONE");

        player.send(new ALERT(about.toString()));
    }

    @Override
    public String getDescription() {
        return " Information about the software powering this retro";
    }
}
