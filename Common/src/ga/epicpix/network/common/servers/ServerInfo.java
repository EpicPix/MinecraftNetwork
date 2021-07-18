package ga.epicpix.network.common.servers;

import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Mongo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerInfo {

    public static final String TYPE = "SERVER_INFO";

    public static final class ServerType {
        private static final ArrayList<ServerType> types = new ArrayList<>();

        public static final ServerType UNKNOWN = new ServerType("UNKNOWN");

        private final String id;

        public ServerType(String id) {
            this.id = id;
            types.add(this);
        }

        public String getId() {
            return id;
        }

        public String toString() {
            return getId();
        }

        public static List<ServerType> getTypes() {
            return Collections.unmodifiableList(types);
        }

        public static ServerType getType(String id) {
            return getType(id, false);
        }

        public static ServerType getType(String id, boolean ignoreCase) {
            for(ServerType type : types) {
                if((ignoreCase && type.getId().equalsIgnoreCase(id)) || (!ignoreCase && type.getId().equals(id))) {
                    return type;
                }
            }
            return null;
        }

    }

    public String id;
    public String type = ServerType.UNKNOWN.getId();
    public int onlinePlayers;
    public int maxPlayers;
    public ServerVersion version;
    public ServerDetails details;
    public long start;
    public transient boolean verified = true;

    public ServerType getType() {
        return ServerType.getType(type);
    }

    public boolean sendSignal(ServerSignal action) {
        if(verified) {
            return Mongo.getCollection("data", "servers").updateOne(new Document().append("id", id), new Document().append("$set", new Document().append("action", action.name()))).getModifiedCount()!=0;
        }else {
            return false;
        }
    }

    public String toString() {
        return "ServerInfo{id=" + CommonUtils.toString(id) + ", type=" + type + ", onlinePlayers=" + onlinePlayers + ", maxPlayers=" + maxPlayers + ", version=" + version + ", details=" + details + ", start=" + start + ", verified=" + verified + "}";
    }
}
