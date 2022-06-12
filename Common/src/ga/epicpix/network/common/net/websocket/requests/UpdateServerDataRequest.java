package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.servers.ServerDetails;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.servers.ServerVersion;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class UpdateServerDataRequest implements WebSocketRequest {

    private final String serverName;
    private final Data data;

    private UpdateServerDataRequest(String serverName, Data data) {
        this.serverName = serverName;
        this.data = data;
    }

    public static final class Data implements SerializableJson {

        private String type;
        private Integer onlinePlayers;
        private Integer maxPlayers;
        private ServerVersion version;
        private ServerDetails details;
        private Long bootMillis;

        public Data setType(ServerInfo.ServerType type) {
            this.type = type.getId();
            return this;
        }

        public Data setOnlinePlayers(int online) {
            onlinePlayers = online;
            return this;
        }

        public Data setMaxPlayers(int max) {
            maxPlayers = max;
            return this;
        }

        public Data setVersion(ServerVersion version) {
            this.version = version;
            return this;
        }

        public Data setDetails(ServerDetails details) {
            this.details = details;
            return this;
        }

        public Data setBootMillis(long millis) {
            bootMillis = millis;
            return this;
        }

        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            if(type!=null) obj.addProperty("type", type);
            if(onlinePlayers!=null) obj.addProperty("onlinePlayers", onlinePlayers);
            if(maxPlayers!=null) obj.addProperty("maxPlayers", maxPlayers);
            if(version!=null) obj.add("version", version.toJson());
            if(details!=null) obj.add("details", details.toJson());
            if(bootMillis!=null) obj.addProperty("bootMillis", bootMillis);
            return obj;
        }
    }

    public static UpdateServerDataRequest build(String serverName, Data data) {
        ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.UPDATE_SERVER);
        return new UpdateServerDataRequest(serverName, data);
    }

    public int getOpcode() {
        return Opcodes.UPDATE_SERVER_DATA;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        obj.add("data", data.toJson());
        return obj;
    }

}
