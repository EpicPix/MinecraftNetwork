package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class RemoveServerRequest implements WebSocketRequest {

    private final String serverName;

    private RemoveServerRequest(String serverName) {
        this.serverName = serverName;
    }

    public static RemoveServerRequest build(String serverName) {
        ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.REMOVE_SERVER);
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
