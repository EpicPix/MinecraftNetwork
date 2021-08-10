package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class ListServersRequest extends RequestData {

    private ListServersRequest() {}

    @CallerSensitive
    public static ListServersRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.LIST_SERVERS, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new ListServersRequest();
    }

    public int getOpcode() {
        return Opcodes.LIST_SERVERS;
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

}
