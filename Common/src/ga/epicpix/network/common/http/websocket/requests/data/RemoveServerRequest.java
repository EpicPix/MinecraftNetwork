package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class RemoveServerRequest extends RequestData {

    private final String serverName;

    private RemoveServerRequest(String serverName) {
        this.serverName = serverName;
    }

    @CallerSensitive
    public static RemoveServerRequest build(String serverName) {
        if(!RequestPolicies.isAllowed(Opcodes.REMOVE_SERVER, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new RemoveServerRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.REMOVE_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        return obj;
    }

}