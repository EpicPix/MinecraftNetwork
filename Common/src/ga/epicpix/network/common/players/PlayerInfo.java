package ga.epicpix.network.common.players;

import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.ranks.RankManager;

import java.util.UUID;

public class PlayerInfo {

    private UUID uuid;
    private String username;
    private String rank;
    private long firstLogin = -1;
    private long lastLogin = -1;

    public UUID getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public Rank getRank() {
        return RankManager.getRank(rank).getValue();
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getLastLogin() {
        return lastLogin;
    }

}
