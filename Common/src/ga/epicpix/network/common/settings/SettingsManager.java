package ga.epicpix.network.common.settings;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.GetSettingOrDefaultRequest;
import ga.epicpix.network.common.websocket.requests.data.GetSettingRequest;
import ga.epicpix.network.common.websocket.requests.data.SetSettingRequest;

public class SettingsManager {

    public static ValueType getSetting(String setting) {
        JsonObject resp = Request.sendRequest(Request.createRequest(Opcodes.GET_SETTING, GetSettingRequest.build(setting)));
        if(!resp.get("ok").getAsBoolean()) {
            return null;
        }
        return ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting"));
    }

    public static ValueType getSettingOrDefault(String setting, ValueType defaults) {
        JsonObject resp = Request.sendRequest(Request.createRequest(Opcodes.GET_SETTING_OR_DEFAULT, GetSettingOrDefaultRequest.build(setting, defaults)));
        if(!resp.get("ok").getAsBoolean()) {
            return defaults;
        }
        return ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting"));
    }

    public static boolean setSetting(String setting, ValueType value) {
        JsonObject resp = Request.sendRequest(Request.createRequest(Opcodes.SET_SETTING, SetSettingRequest.build(setting, value)));
        return resp.get("ok").getAsBoolean();
    }

}
