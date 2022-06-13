package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetModulesRequest implements WebSocketRequest {

    private GetModulesRequest() {}


    @ChecksPermission(ModulePermission.GET_MODULES)
    public static GetModulesRequest build() {
        ModuleLoader.checkModulePermission(ModulePermission.GET_MODULES);
        return new GetModulesRequest();
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

    public int getOpcode() {
        return Opcodes.GET_MODULES;
    }

}
