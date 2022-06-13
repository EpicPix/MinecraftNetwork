package ga.epicpix.network.common.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.values.ValueType;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.requests.GetSettingOrDefaultRequest;
import ga.epicpix.network.common.net.websocket.requests.GetSettingRequest;
import ga.epicpix.network.common.net.websocket.requests.GetSettingsRequest;
import ga.epicpix.network.common.net.websocket.requests.SetSettingRequest;

import java.util.HashMap;

public final class SettingsManager {

    @ChecksPermission(ModulePermission.GET_SETTING)
    public static Errorable<ValueType> getSetting(String setting) {
        JsonObject resp = WebSocketRequester.sendRequest(GetSettingRequest.build(setting));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting")));
    }

    @ChecksPermission({ModulePermission.GET_SETTING, ModulePermission.SET_SETTING})
    public static Errorable<ValueType> getSettingOrDefault(String setting, ValueType defaults) {
        JsonObject resp = WebSocketRequester.sendRequest(GetSettingOrDefaultRequest.build(setting, defaults));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(ValueType.getValueTypeFromJson(resp.getAsJsonObject("setting")));
    }

    @ChecksPermission(ModulePermission.SET_SETTING)
    public static Errorable<Boolean> setSetting(String setting, ValueType value) {
        JsonObject resp = WebSocketRequester.sendRequest(SetSettingRequest.build(setting, value));
        if(!resp.get("ok").getAsBoolean()) {
            return new Errorable<>(resp.get("errno").getAsInt());
        }
        return new Errorable<>(resp.get("ok").getAsBoolean());
    }

    @ChecksPermission(ModulePermission.GET_SETTINGS)
    public static Errorable<HashMap<String, ValueType>> getSettings() {
        JsonObject resp = WebSocketRequester.sendRequest(GetSettingsRequest.build());
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
