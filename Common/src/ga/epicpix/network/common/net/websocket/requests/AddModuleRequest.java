package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.Base64;

public class AddModuleRequest implements WebSocketRequest {

    private final String module;

    private AddModuleRequest(String module) {
        this.module = module;
    }

    public static AddModuleRequest build(byte[] module) {
        return new AddModuleRequest(Base64.getEncoder().encodeToString(module));
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("module", module);
        return obj;
    }

    public int getOpcode() {
        return Opcodes.ADD_MODULE;
    }

}
