package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class RemoveServerRequest extends RequestData {

    private RemoveServerRequest() {
    }

    public static RemoveServerRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.REMOVE_SERVER, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new RemoveServerRequest();
    }

    public int getOpcode() {
        return Opcodes.REMOVE_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        return obj;
    }

}
