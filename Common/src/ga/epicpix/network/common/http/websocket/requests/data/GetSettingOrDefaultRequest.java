package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetSettingOrDefaultRequest extends RequestData {

    private final String setting;
    private final ValueType def;

    private GetSettingOrDefaultRequest(String setting, ValueType def) {
        this.setting = setting;
        this.def = def;
    }

    @CallerSensitive
    public static GetSettingOrDefaultRequest build(String setting, ValueType def) {
        if(!RequestPolicies.isAllowed(Opcodes.GET_SETTING_OR_DEFAULT, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
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
