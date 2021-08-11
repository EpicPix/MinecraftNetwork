package ga.epicpix.network.common.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.Opcodes;
import ga.epicpix.network.common.net.websocket.requests.WebsocketRequest;
import ga.epicpix.network.common.net.websocket.requests.data.GetSettingOrDefaultRequest;
import ga.epicpix.network.common.net.websocket.requests.data.GetSettingRequest;
import ga.epicpix.network.common.net.websocket.requests.data.GetSettingsRequest;
import ga.epicpix.network.common.net.websocket.requests.data.SetSettingRequest;

import java.util.HashMap;

public class SettingsManager {

    public static Errorable<ValueType> getSetting(String setting) {
        JsonObject resp = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_SETTING, GetSettingRequest.build(setting)));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting")));
    }

    public static Errorable<ValueType> getSettingOrDefault(String setting, ValueType defaults) {
        JsonObject resp = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_SETTING_OR_DEFAULT, GetSettingOrDefaultRequest.build(setting, defaults)));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting")));
    }

    public static Errorable<Boolean> setSetting(String setting, ValueType value) {
        JsonObject resp = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.SET_SETTING, SetSettingRequest.build(setting, value)));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(resp.get("ok").getAsBoolean());
    }

    public static Errorable<HashMap<String, ValueType>> getSettings() {
        JsonObject resp = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_SETTINGS, GetSettingsRequest.build()));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        var map = new HashMap<String, ValueType>();
        for(JsonElement e : resp.getAsJsonArray("settings")) {
            JsonObject o = (JsonObject) e;
            map.put(o.get("stringVersion").getAsString(), ValueType.getValueTypeFromJson(o.getAsJsonObject("value")));
        }
        return new Errorable<>(map);
    }

}
