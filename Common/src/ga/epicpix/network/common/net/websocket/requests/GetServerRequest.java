package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetServerRequest implements WebSocketRequest {

    private final String server;

    private GetServerRequest(String server) {
        this.server = server;
    }

    @ChecksPermission(ModulePermission.GET_SERVER_INFO)
    public static GetServerRequest build(String server) {
        ModuleLoader.checkModulePermission(ModulePermission.GET_SERVER_INFO);
        return new GetServerRequest(server);
    }

    public int getOpcode() {
        return Opcodes.GET_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", server);
        return obj;
    }

}
