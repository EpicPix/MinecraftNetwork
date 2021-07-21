package ga.epicpix.network.common.websocket.requests.data;

import com.google.gson.JsonObject;

public abstract class RequestData {

    RequestData() {}

    public abstract int getOpcode();
    public abstract JsonObject toJson();

}
