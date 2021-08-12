package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.*;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class SetSettingRequest implements WebSocketRequest {

    private final String setting;
    private final ValueType value;

    private SetSettingRequest(String setting, ValueType value) {
        this.setting = setting;
        this.value = value;
    }

    public static SetSettingRequest build(String server, ValueType value) {
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
