package ga.epicpix.network.common.http.websocket;

import com.google.gson.JsonObject;

public interface ServerHook {

    void handle(int opcode, JsonObject data, Requester requester);
}
