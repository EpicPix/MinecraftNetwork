package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetSettingsRequest extends RequestData {

    private GetSettingsRequest() {}

    @CallerSensitive
    public static GetSettingsRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.GET_SETTINGS, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetSettingsRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_SETTINGS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
