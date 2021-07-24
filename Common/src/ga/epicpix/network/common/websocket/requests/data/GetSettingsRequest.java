package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class GetSettingsRequest extends RequestData {

    private GetSettingsRequest() {}

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