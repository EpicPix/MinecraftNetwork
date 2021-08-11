package ga.epicpix.network.common.http.websocket.requests.data;

import static ga.epicpix.network.common.http.websocket.requests.data.UpdateRankRequest.Data;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class CreateRankRequest extends RequestData {

    private final String rank;
    private final Data data;

    private CreateRankRequest(String rank, Data data) {
        this.rank = rank;
        this.data = data;
    }

    @CallerSensitive
    public static CreateRankRequest build(String serverName, Data data) {
        if(!RequestPolicies.isAllowed(Opcodes.CREATE_RANK, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new CreateRankRequest(serverName, data);
    }

    public int getOpcode() {
        return Opcodes.CREATE_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        obj.add("data", data.toJson());
        return obj;
    }

}