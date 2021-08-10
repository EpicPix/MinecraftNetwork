package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class GetDefaultRankRequest extends RequestData {

    private GetDefaultRankRequest() {}

    @CallerSensitive
    public static GetDefaultRankRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.GET_DEFAULT_RANK, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetDefaultRankRequest();
    }

    public int getOpcode() {
        return Opcodes.GET_DEFAULT_RANK;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
