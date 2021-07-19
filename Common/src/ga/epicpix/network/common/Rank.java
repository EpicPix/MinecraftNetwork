package ga.epicpix.network.common;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Rank {

    public static final String TYPE = "RANK";

    @SerializedName("default")
    public boolean def;
    public String id;
    public int priority;
    public boolean opAccess;
    public ChatComponent[] prefix = new ChatComponent[0];
    public ChatComponent[] suffix = new ChatComponent[0];
    public String[] permissions = new String[0];
    public String nameColor = "white";
    public String chatColor = "white";

    public Rank setDefault(boolean def) {
        this.def = def;
        return this;
    }

    public Rank setId(String id) {
        this.id = id;
        return this;
    }

    public Rank setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Rank setOpAccess(boolean access) {
        opAccess = access;
        return this;
    }

    public Rank setPrefix(ChatComponent... prefix) {
        this.prefix = prefix;
        return this;
    }

    public Rank setSuffix(ChatComponent... suffix) {
        this.suffix = suffix;
        return this;
    }

    public Rank setPermissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public Rank setChatColor(String color) {
        chatColor = color;
        return this;
    }

    public Rank setNameColor(String color) {
        nameColor = color;
        return this;
    }

    public static ArrayList<Rank> getRanks() {
        MongoCollection<Document> cdranks = Mongo.getCollection("data", "ranks");
        if(cdranks.countDocuments()!=0) {
            ArrayList<Document> dranks = CommonUtils.iteratorToList(cdranks.find().iterator());
            ArrayList<Rank> ranks = CommonUtils.documentsToObjects(dranks, Rank.class);
            boolean hasDefault = false;
            for(Rank rank : ranks) {
                if(rank.def) {
                    hasDefault = true;
                    break;
                }
            }
            if(!hasDefault) {
                Document first = dranks.remove(0);
                Document updates = CommonUtils.toDocument(CommonUtils.documentToObject(first, Rank.class).setDefault(true));
                dranks.add(0, updates);
                cdranks.updateOne(new Document().append("_id", first.getObjectId("_id")), Updates.set("default", true), new UpdateOptions().upsert(true));
                ranks = CommonUtils.documentsToObjects(dranks, Rank.class);
            }
            return ranks;
        }else {
            Rank rank = new Rank().setDefault(true).setId("DEFAULT").setPriority(0).setOpAccess(false).setPrefix().setSuffix().setPermissions().setNameColor("white").setChatColor("white");
            cdranks.insertOne(CommonUtils.toDocument(rank));
            return new ArrayList<>(Collections.singletonList(rank));
        }
    }

    public static Rank getRankByName(String rank) {
        for(Rank irank : getRanks()) {
            if(irank.id.equals(rank)) {
                return irank;
            }
        }
        return null;
    }

    public String toString() {
        return "Rank{default=" + def + ", id=" + CommonUtils.toString(id) + ", priority=" + priority + ", opAccess=" + opAccess + ", prefix=" + Arrays.toString(prefix) + ", suffix=" + Arrays.toString(suffix) + ", chatColor=" + chatColor + "}";
    }
}
