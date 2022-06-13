package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class ListServersRequest implements WebSocketRequest {

    private ListServersRequest() {}

    @ChecksPermission(ModulePermission.LIST_SERVERS)
    public static ListServersRequest build() {
        ModuleLoader.checkModulePermission(ModulePermission.LIST_SERVERS);
        return new ListServersRequest();
    }

    public int getOpcode() {
        return Opcodes.LIST_SERVERS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
