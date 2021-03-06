package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetRankRequest implements WebSocketRequest {

    private final String rank;

    private GetRankRequest(String rank) {
        this.rank = rank;
    }

    @ChecksPermission(ModulePermission.GET_RANK)
    public static GetRankRequest build(String rank) {
        ModuleLoader.checkModulePermission(ModulePermission.GET_RANK);
        return new GetRankRequest(rank);
    }

    public int getOpcode() {
        return Opcodes.GET_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        return obj;
    }

}
