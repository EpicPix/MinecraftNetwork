package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetSettingRequest extends RequestData {

    private final String setting;

    private GetSettingRequest(String setting) {
        this.setting = setting;
    }

    @CallerSensitive
    public static GetSettingRequest build(String setting) {
        if(!RequestPolicies.isAllowed(Opcodes.GET_SETTING, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
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
