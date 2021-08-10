package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.RequestPolicies;

import static ga.epicpix.network.common.websocket.requests.data.UpdateRankRequest.Data;

public class DeleteRankRequest extends RequestData {

    private final String rank;

    private DeleteRankRequest(String rank) {
        this.rank = rank;
    }

    @CallerSensitive
    public static DeleteRankRequest build(String serverName) {
        if(!RequestPolicies.isAllowed(Opcodes.DELETE_RANK, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new DeleteRankRequest(serverName);
    }

    public int getOpcode() {
        return Opcodes.DELETE_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        return obj;
    }

}
