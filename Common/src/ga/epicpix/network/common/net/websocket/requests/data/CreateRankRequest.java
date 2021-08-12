package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

import static ga.epicpix.network.common.net.websocket.requests.data.UpdateRankRequest.Data;

public class CreateRankRequest implements WebSocketRequest {

    private final String rank;
    private final Data data;

    private CreateRankRequest(String rank, Data data) {
        this.rank = rank;
        this.data = data;
    }

    public static CreateRankRequest build(String serverName, Data data) {
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
