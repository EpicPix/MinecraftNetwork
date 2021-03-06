package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.UUID;

public class GetPlayerRequest implements WebSocketRequest {

    private final UUID uuid;
    private final String username;

    private GetPlayerRequest(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @ChecksPermission(ModulePermission.GET_PLAYER)
    public static GetPlayerRequest build(UUID uuid, String username) {
        ModuleLoader.checkModulePermission(ModulePermission.GET_PLAYER);
        if(uuid == null && username == null) {
            throw new IllegalArgumentException("UUID or Username must not be null");
        }
        return new GetPlayerRequest(uuid, username);
    }

    public int getOpcode() {
        return Opcodes.GET_PLAYER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if(uuid!=null) obj.addProperty("uuid", uuid.toString());
        if(username!=null) obj.addProperty("username", username);
        return obj;
    }
}
