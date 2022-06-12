package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetModuleRequest implements WebSocketRequest {

    private final String id;
    private final String version;

    private GetModuleRequest(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public static GetModuleRequest build(String id, String version) {
        ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.GET_MODULE);
        return new GetModuleRequest(id, version);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("version", version);
        return obj;
    }

    public int getOpcode() {
        return Opcodes.GET_MODULE;
    }

}
