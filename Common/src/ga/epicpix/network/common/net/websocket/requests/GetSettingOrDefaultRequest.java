package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModuleLoader;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetSettingOrDefaultRequest implements WebSocketRequest {

    private final String setting;
    private final ValueType def;

    private GetSettingOrDefaultRequest(String setting, ValueType def) {
        this.setting = setting;
        this.def = def;
    }

    @ChecksPermission({ModulePermission.GET_SETTING, ModulePermission.SET_SETTING})
    public static GetSettingOrDefaultRequest build(String setting, ValueType def) {
        ModuleLoader.checkModulePermission(ModulePermission.GET_SETTING);
        ModuleLoader.checkModulePermission(ModulePermission.SET_SETTING);
        return new GetSettingOrDefaultRequest(setting, def);
    }

    public int getOpcode() {
        return Opcodes.GET_SETTING_OR_DEFAULT;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("setting", setting);
        obj.add("default", ValueType.getJsonFromValueType(def));
        return obj;
    }

}
