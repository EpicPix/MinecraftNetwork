package ga.epicpix.network.common.modules;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.AddModuleRequest;
import ga.epicpix.network.common.net.websocket.requests.GetModuleRequest;
import ga.epicpix.network.common.net.websocket.requests.GetModulesRequest;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

public class ModuleManager {

    public static Errorable<ArrayList<ModuleData>> getModules() {
        JsonObject obj = WebSocketRequester.sendRequest(GetModulesRequest.build());
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        ArrayList<ModuleData> data = new ArrayList<>();
        Collections.addAll(data, new Gson().fromJson(obj.getAsJsonArray("modules"), ModuleData[].class));
        return new Errorable<>(data);
    }

    public static Errorable<ModuleFile> getModule(String id, String version) {
        JsonObject obj = WebSocketRequester.sendRequest(GetModuleRequest.build(id, version));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new ModuleFile(Base64.getDecoder().decode(obj.get("module").getAsString())));
    }

    public static Errorable<ModuleData> addModule(byte[] moduleBytes) {
        JsonObject obj = WebSocketRequester.sendRequest(AddModuleRequest.build(moduleBytes));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(new Gson().fromJson(obj.getAsJsonObject("module"), ModuleData.class));
    }

}
