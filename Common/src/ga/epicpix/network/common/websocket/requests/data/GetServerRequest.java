package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class GetServerRequest extends RequestData {

    private final String server;

    private GetServerRequest(String server) {
        this.server = server;
    }

    @CallerSensitive
    public static GetServerRequest build(String server) {
        if(!RequestPolicies.isAllowed(Opcodes.GET_SERVER, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetServerRequest(server);
    }

    public int getOpcode() {
        return Opcodes.GET_SERVER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", server);
        return obj;
    }

}
