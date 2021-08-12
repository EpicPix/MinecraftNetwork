package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetSettingOrDefaultRequest implements WebSocketRequest {

    private final String setting;
    private final ValueType def;

    private GetSettingOrDefaultRequest(String setting, ValueType def) {
        this.setting = setting;
        this.def = def;
    }

    public static GetSettingOrDefaultRequest build(String setting, ValueType def) {
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
