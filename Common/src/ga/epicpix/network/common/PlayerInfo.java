package ga.epicpix.network.common;

import com.mongodb.client.MongoCollection;
import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.ranks.RankManager;
import org.bson.Document;

import java.util.UUID;

public class PlayerInfo {

    public String uuid;
    public String username;
    public String rank;
    public long firstLogin = -1;
    public long lastLogin = -1;

    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    public Rank getRank() {
        return RankManager.getRank(rank);
    }

    public PlayerInfo populate(UUID uuid, String username, Rank rank) {
        this.uuid = uuid.toString();
        this.username = username;
        this.rank = rank.getId();
        if(firstLogin==-1) {
            firstLogin = System.currentTimeMillis();
        }
        if(lastLogin==-1) {
            lastLogin = System.currentTimeMillis();
        }
        return this;
    }

    public static PlayerInfo getPlayerInfo(UUID player) {
        MongoCollection<Document> players = Mongo.getCollection("data", "players");
        Document pl = players.find(new Document().append("uuid", player.toString())).first();
        return CommonUtils.documentToObject(pl, PlayerInfo.class);
    }

    public static PlayerInfo updatePlayerInfo(PlayerInfo info) {
        MongoCollection<Document> players = Mongo.getCollection("data", "players");
        Document pl = players.find(new Document().append("uuid", info.uuid)).first();
        if(pl==null) {
            players.insertOne(CommonUtils.toDocument(info));
        }else {
            players.updateOne(new Document().append("uuid", info.uuid), CommonUtils.set(CommonUtils.toDocument(info)));
        }
        return info;
    }

}
