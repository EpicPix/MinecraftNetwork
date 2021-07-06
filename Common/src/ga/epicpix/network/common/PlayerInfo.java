package ga.epicpix.network.common;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.UUID;

public class PlayerInfo {

    public static final String TYPE = "PLAYER_INFO";

    public String uuid;
    public String username;
    public String rank;
    public String language;
    public long firstLogin = -1;

    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    public Rank getRank() {
        return Rank.getRankByName(rank);
    }

    public Language getLanguage() {
        return Language.getLanguage(language);
    }

    public PlayerInfo populate(UUID uuid, String username, Rank rank, Language language) {
        this.uuid = uuid.toString();
        this.username = username;
        this.rank = rank.id;
        this.language = language.id;
        if(firstLogin==-1) {
            firstLogin = System.currentTimeMillis();
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
            players.updateOne(new Document().append("uuid", info.uuid), CommonUtils.toDocument(info));
        }
        return info;
    }

}
