package ga.epicpix.network.common.net.websocket.requests.data;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Opcodes;

public class DeleteRankRequest implements WebSocketRequest {

    private final String rank;

    private DeleteRankRequest(String rank) {
        this.rank = rank;
    }

    public static DeleteRankRequest build(String serverName) {
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
