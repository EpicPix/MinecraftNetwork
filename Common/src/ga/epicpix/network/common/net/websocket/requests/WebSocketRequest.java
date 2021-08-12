package ga.epicpix.network.common.net.websocket.requests;

import ga.epicpix.network.common.SerializableJson;
import ga.epicpix.network.common.net.Request;

public interface WebSocketRequest extends Request, SerializableJson {

    int getOpcode();

}
