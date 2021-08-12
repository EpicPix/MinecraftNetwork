package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class GetRankRequest implements WebSocketRequest {

    private final String rank;

    private GetRankRequest(String rank) {
        this.rank = rank;
    }

    public static GetRankRequest build(String rank) {
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
