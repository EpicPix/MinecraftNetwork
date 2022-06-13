package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

import java.util.Base64;

public class AddModuleRequest implements WebSocketRequest {

    private final String module;

    private AddModuleRequest(String module) {
        this.module = module;
    }

    @ChecksPermission(ModulePermission.ADD_MODULE)
    public static AddModuleRequest build(byte[] module) {
        ModuleLoader.checkModulePermission(ModulePermission.ADD_MODULE);
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
