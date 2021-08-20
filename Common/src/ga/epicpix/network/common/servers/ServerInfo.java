package ga.epicpix.network.common.servers;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerInfo {

    public enum ServerSignal {

        STOP;

        public static ServerSignal getSignal(String signal) {
            signal = signal.toUpperCase();
            for(ServerSignal sig : values()) {
                if(sig.name().equals(signal)) {
                    return sig;
                }
            }
            return null;
        }

    }

    public static class ServerType {
        private static final List<ServerType> types = new ArrayList<>();

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
            for (ServerType type : types) {
                if(ignoreCase && type.getId().equalsIgnoreCase(id)) {
                    return type;
                }else if(!ignoreCase && type.getId().equals(id)) {
                    return type;
                }
            }
            return null;
        }

    }

    public final String id;
    public String type = ServerType.UNKNOWN.getId();
    public int onlinePlayers;
    public int maxPlayers;
    public ServerVersion version;
    public ServerDetails details;
    public long start;

    public ServerInfo(String id) {
        this.id = id;
    }

    public ServerType getType() {
        return ServerType.getType(type);
    }

    public String toString() {
        return "ServerInfo{id=" + CommonUtils.toString(id) + ", type=" + type + ", onlinePlayers=" + onlinePlayers + ", maxPlayers=" + maxPlayers + ", version=" + version + ", details=" + details + ", start=" + start + "}";
    }

    public static ServerInfo fromJson(JsonObject obj) {
        ServerInfo info = new ServerInfo(obj.get("id").getAsString());
        info.type = obj.get("type").getAsString();
        info.onlinePlayers = obj.get("onlinePlayers").getAsInt();
        info.maxPlayers = obj.get("maxPlayers").getAsInt();
        info.version = ServerVersion.getVersionByName(obj.getAsJsonObject("version").get("stringVersion").getAsString());
        info.details = new ServerDetails(obj.getAsJsonObject("details").get("ip").getAsString(), obj.getAsJsonObject("details").get("port").getAsInt());
        info.start = obj.get("bootMillis").getAsLong();
        return info;
    }
}
