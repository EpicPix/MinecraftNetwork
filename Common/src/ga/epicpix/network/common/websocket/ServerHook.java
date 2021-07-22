package ga.epicpix.network.common.websocket;

import com.google.gson.JsonObject;

public interface ServerHook {

    void handle(int opcode, JsonObject data, Requester requester);
}
