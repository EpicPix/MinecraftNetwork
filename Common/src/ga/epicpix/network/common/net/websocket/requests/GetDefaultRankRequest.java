package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetDefaultRankRequest implements WebSocketRequest {

    private GetDefaultRankRequest() {}

    @ChecksPermission(ModulePermission.GET_DEFAULT_RANK)
    public static GetDefaultRankRequest build() {
        ModuleLoader.checkModulePermission(ModulePermission.GET_DEFAULT_RANK);
        return new GetDefaultRankRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_DEFAULT_RANK;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
