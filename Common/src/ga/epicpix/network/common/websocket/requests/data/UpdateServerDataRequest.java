package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

public class UpdateServerDataRequest extends RequestData {

    private UpdateServerDataRequest() {
    }

    public static UpdateServerDataRequest build() {
        if(!RequestPolicies.isAllowed(Opcodes.UPDATE_SERVER_DATA, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new UpdateServerDataRequest();
    }

    public int getOpcode() {
        return Opcodes.UPDATE_SERVER_DATA;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        return obj;
    }

}
