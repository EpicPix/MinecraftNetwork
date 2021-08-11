package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class MakeWebSocketServerOwnerRequest extends RequestData {

    private final String serverName;

    private MakeWebSocketServerOwnerRequest(String serverName) {
        this.serverName = serverName;
    }

    @CallerSensitive
    public static MakeWebSocketServerOwnerRequest build(String serverName) {
        if(!RequestPolicies.isAllowed(Opcodes.MAKE_WEB_SOCKET_SERVER_OWNER, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new MakeWebSocketServerOwnerRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.MAKE_WEB_SOCKET_SERVER_OWNER;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", serverName);
        return obj;
    }

}
