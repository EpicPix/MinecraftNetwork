package ga.epicpix.network.common.http.websocket.requests.data;

import ga.epicpix.network.common.SerializableJson;

public abstract class RequestData implements SerializableJson {

    RequestData() {}

    public abstract int getOpcode();

}
