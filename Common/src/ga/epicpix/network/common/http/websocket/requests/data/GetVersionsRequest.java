package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetVersionsRequest extends RequestData {

    private GetVersionsRequest() {}

    @CallerSensitive
    public static GetVersionsRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.GET_VERSIONS, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetVersionsRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_VERSIONS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
