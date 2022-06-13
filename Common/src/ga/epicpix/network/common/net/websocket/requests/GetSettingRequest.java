package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetSettingRequest implements WebSocketRequest {

    private final String setting;

    private GetSettingRequest(String setting) {
        this.setting = setting;
    }

    @ChecksPermission(ModulePermission.GET_SETTING)
    public static GetSettingRequest build(String setting) {
        ModuleLoader.checkModulePermission(ModulePermission.GET_SETTING);
        return new GetSettingRequest(setting);
    }

    public int getOpcode() {
        return Opcodes.GET_SETTING;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("setting", setting);
        return obj;
    }

}
