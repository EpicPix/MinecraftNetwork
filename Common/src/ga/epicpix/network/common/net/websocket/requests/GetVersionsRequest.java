package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetVersionsRequest implements WebSocketRequest {

    private GetVersionsRequest() {}

    @ChecksPermission(ModulePermission.GET_VERSIONS)
    public static GetVersionsRequest build() {
        ModuleLoader.checkModulePermission(ModulePermission.GET_VERSIONS);
        return new GetVersionsRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_VERSIONS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
