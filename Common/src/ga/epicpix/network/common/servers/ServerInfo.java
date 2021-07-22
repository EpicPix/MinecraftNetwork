package ga.epicpix.network.common.servers;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.RemoveServerRequest;
import ga.epicpix.network.common.websocket.requests.data.UpdateServerDataRequest;

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

    public static record ServerType(String id) {
        private static final ArrayList<ServerType> types = new ArrayList<>();

        public static final ServerType UNKNOWN = new ServerType("UNKNOWN");

        public ServerType {
            types.add(this);
        }

        public String toString() {
            return id();
        }

        public static List<ServerType> getTypes() {
            return Collections.unmodifiableList(types);
        }

        public static ServerType getType(String id) {
            return getType(id, false);
        }

        public static ServerType getType(String id, boolean ignoreCase) {
            for (ServerType type : types) {
                if ((ignoreCase && type.id().equalsIgnoreCase(id)) || (!ignoreCase && type.id().equals(id))) {
                    return type;
                }
            }
            return null;
        }

    }

    public final String id;
    public String type = ServerType.UNKNOWN.id();
    public int onlinePlayers;
    public int maxPlayers;
    public ServerVersion version;
    public ServerDetails details;
    public long start;

    public static JsonObject updateServer(String server, UpdateServerDataRequest.Data data) {
        if(server==null) {
            throw new NullPointerException("Server is null");
        }
        if(server.isEmpty()) {
            throw new IllegalArgumentException("Server name is empty");
        }
        if(data==null) {
            throw new NullPointerException("Data is null");
        }
        return Request.sendRequest(Request.createRequest(Opcodes.UPDATE_SERVER_DATA, UpdateServerDataRequest.build(server, data)));
    }

    public static JsonObject removeServer(String server) {
        if(server==null) {
            throw new NullPointerException("Server is null");
        }
        if(server.isEmpty()) {
            throw new IllegalArgumentException("Server name is empty");
        }
        return Request.sendRequest(Request.createRequest(Opcodes.REMOVE_SERVER, RemoveServerRequest.build(server)));
    }

    public static JsonObject makeWebSocketServerOwner(String server) {
        //TODO: Implement makeWebSocketServerOwner
        return null;
    }

    public static JsonObject sendSignal(String server, ServerSignal signal) {
        //TODO: Implement sendSignal
        return null;
    }

    public ServerInfo(String id) {
        this.id = id;
    }

    public ServerType getType() {
        return ServerType.getType(type);
    }

    public String toString() {
        return "ServerInfo{id=" + CommonUtils.toString(id) + ", type=" + type + ", onlinePlayers=" + onlinePlayers + ", maxPlayers=" + maxPlayers + ", version=" + version + ", details=" + details + ", start=" + start + "}";
    }
}
