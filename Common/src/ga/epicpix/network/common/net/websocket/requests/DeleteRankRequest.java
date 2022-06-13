package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class DeleteRankRequest implements WebSocketRequest {

    private final String rank;

    private DeleteRankRequest(String rank) {
        this.rank = rank;
    }

    @ChecksPermission(ModulePermission.DELETE_RANK)
    public static DeleteRankRequest build(String serverName) {
        ModuleLoader.checkModulePermission(ModulePermission.DELETE_RANK);
        return new DeleteRankRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.DELETE_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        return obj;
    }

}
