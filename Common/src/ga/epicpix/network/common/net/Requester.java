package ga.epicpix.network.common.net;

import com.google.gson.JsonObject;

public interface Requester {

    JsonObject nSendRequest(Request req);

}
