package org.alexdev.havana.game.badges;

import org.alexdev.havana.dao.mysql.BadgeDao;

public class BadgeDefinition {
    private String badgeCode;
    private String badgeName;
    private String badgeDescription;
    private int users;

    public BadgeDefinition(String badgeCode, String badgeName, String badgeDescription) {
        this.badgeCode = badgeCode;
        this.badgeName = badgeName;
        this.badgeDescription = badgeDescription;
        this.users = BadgeDao.getAmountOfUsersWithBadge(this.badgeCode);
    }

    public String getBadgeCode() {
        return badgeCode;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public String getBadgeDescription() {
        return badgeDescription;
    }

    public int getUsers() {
        return this.users;
    }

    public void setUsers(int i) {
        this.users = i;
    }
}
