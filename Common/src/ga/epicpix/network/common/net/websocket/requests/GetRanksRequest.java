package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetRanksRequest implements WebSocketRequest {

    private GetRanksRequest() {}

    @ChecksPermission(ModulePermission.GET_RANKS)
    public static GetRanksRequest build() {
        ModuleLoader.checkModulePermission(ModulePermission.GET_RANKS);
        return new GetRanksRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_RANKS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
