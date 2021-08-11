package ga.epicpix.network.common.http.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.http.websocket.Opcodes;
import ga.epicpix.network.common.http.websocket.requests.RequestPolicies;

public class GetRankRequest extends RequestData {

    private final String rank;

    private GetRankRequest(String rank) {
        this.rank = rank;
    }

    @CallerSensitive
    public static GetRankRequest build(String rank) {
        if(!RequestPolicies.isAllowed(Opcodes.GET_RANK, Reflection.getCaller())) {
            throw new SecurityException("Cannot build this request data!");
        }
        return new GetRankRequest(rank);
    }

    public int getOpcode() {
        return Opcodes.GET_RANK;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("rank", rank);
        return obj;
    }

}
