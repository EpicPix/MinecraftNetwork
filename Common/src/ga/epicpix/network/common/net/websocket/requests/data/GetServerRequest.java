package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetServerRequest extends RequestData {

    private final String server;

    private GetServerRequest(String server) {
        this.server = server;
    }

    public static GetServerRequest build(String server) {
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