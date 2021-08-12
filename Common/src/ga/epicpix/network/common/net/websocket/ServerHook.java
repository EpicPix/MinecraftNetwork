package ga.epicpix.network.common.net.websocket;

import com.google.gson.JsonObject;

public interface ServerHook {

    void handle(int opcode, JsonObject data, WebSocketRequester requester);
}
