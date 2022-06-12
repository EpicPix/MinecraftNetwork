package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetSettingsRequest implements WebSocketRequest {

    private GetSettingsRequest() {}

    public static GetSettingsRequest build() {
        ModuleLoader.checkModulePermission(ModuleLoader.ModulePermission.GET_SETTINGS);
        return new GetSettingsRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_SETTINGS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
