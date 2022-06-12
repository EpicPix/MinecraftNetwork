package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class MakeWebSocketServerOwnerRequest implements WebSocketRequest {

    private final String serverName;

    private MakeWebSocketServerOwnerRequest(String serverName) {
        this.serverName = serverName;
    }

    public static MakeWebSocketServerOwnerRequest build(String serverName) {
        var moduleClass = Reflection.getModuleCaller();
        if(moduleClass != null) throw new SecurityException("Cannot create MakeWebSocketServerOwnerRequest from a module");
        return new MakeWebSocketServerOwnerRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.MAKE_WEB_SOCKET_SERVER_OWNER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        return obj;
    }

}
