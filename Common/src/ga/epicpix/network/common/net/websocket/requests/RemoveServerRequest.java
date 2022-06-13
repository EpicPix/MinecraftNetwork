package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class RemoveServerRequest implements WebSocketRequest {

    private final String serverName;

    private RemoveServerRequest(String serverName) {
        this.serverName = serverName;
    }

    @ChecksPermission(ModulePermission.REMOVE_SERVER)
    public static RemoveServerRequest build(String serverName) {
        ModuleLoader.checkModulePermission(ModulePermission.REMOVE_SERVER);
        return new RemoveServerRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.REMOVE_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        return obj;
    }

}
