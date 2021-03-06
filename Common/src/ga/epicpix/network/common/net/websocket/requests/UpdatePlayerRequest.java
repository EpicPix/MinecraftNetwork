package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.UUID;

public class UpdatePlayerRequest implements WebSocketRequest {

    private final UUID uuid;
    private final String username;
    private final Data data;

    private UpdatePlayerRequest(UUID uuid, String username, Data data) {
        this.uuid = uuid;
        this.username = username;
        this.data = data;
    }

    public static final class Data implements SerializableJson {

        private String username;
        private String rank;
        private Long firstLogin;
        private Long lastLogin;

        public Data setUsername(String username) {
            this.username = username;
            return this;
        }

        public Data setRank(String rank) {
            this.rank = rank;
            return this;
        }

        public Data setFirstLogin(long firstLogin) {
            this.firstLogin = firstLogin;
            return this;
        }

        public Data setLastLogin(long lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            if(username!=null) obj.addProperty("username", username);
            if(rank!=null) obj.addProperty("rank", rank);
            if(firstLogin!=null) obj.addProperty("firstLogin", firstLogin);
            if(lastLogin!=null) obj.addProperty("lastLogin", lastLogin);
            return obj;
        }
    }

    @ChecksPermission(ModulePermission.UPDATE_PLAYER)
    public static UpdatePlayerRequest build(UUID uuid, String username, Data data) {
        ModuleLoader.checkModulePermission(ModulePermission.UPDATE_PLAYER);
        if(uuid==null && username==null) {
            throw new IllegalArgumentException("UUID or Username must not be null");
        }
        return new UpdatePlayerRequest(uuid, username, data);
    }

    public int getOpcode() {
        return Opcodes.UPDATE_PLAYER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if(uuid!=null) obj.addProperty("uuid", uuid.toString());
        if(username!=null) obj.addProperty("username", username);
        obj.add("data", data.toJson());
        return obj;
    }

}
