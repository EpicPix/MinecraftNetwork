package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.*;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class SetSettingRequest extends RequestData {

    private final String setting;
    private final ValueType value;

    private SetSettingRequest(String setting, ValueType value) {
        this.setting = setting;
        this.value = value;
    }

    @CallerSensitive
    public static SetSettingRequest build(String server, ValueType value) {
        if(!RequestPolicies.isAllowed(Opcodes.SET_SETTING, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new SetSettingRequest(server, value);
    }

    public int getOpcode() {
        return Opcodes.SET_SETTING;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("setting", setting);
        obj.add("value", ValueType.getJsonFromValueType(value));
        return obj;
    }

}