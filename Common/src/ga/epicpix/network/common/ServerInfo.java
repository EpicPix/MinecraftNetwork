package ga.epicpix.network.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerInfo {

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
    public ServerType type = ServerType.UNKNOWN;
    public int maxPlayers;
    public ServerVersion version;
    public ServerDetails details;

    public String toString() {
        return "ServerInfo{id=" + CommonUtils.toString(id) + ", type=" + type + ", maxPlayers=" + maxPlayers + ", version=" + version + ", details=" + details + "}";
    }
}
