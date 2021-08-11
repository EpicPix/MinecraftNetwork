package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetRanksRequest extends RequestData {

    private GetRanksRequest() {}

    @CallerSensitive
    public static GetRanksRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.GET_RANKS, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetRanksRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_RANKS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
