package ga.epicpix.network.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerInfo {

    public static class ServerType {
        public static final ServerType UNKNOWN = new ServerType("UNKNOWN");

        private static final ArrayList<ServerType> types = new ArrayList<>();

        private final String id;

        public ServerType(String id) {
            this.id = id;
            types.add(this);
        }

        public final String getId() {
            return id;
        }

        public final String toString() {
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

    public String toString() {
        return "ServerInfo{id=" + (id==null?"null":("'" + id + "'")) + ", type=" + type + ", maxPlayers=" + maxPlayers + "}";
    }
}
