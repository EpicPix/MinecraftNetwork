package ga.epicpix.network.common.net.http;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.Request;
import ga.epicpix.network.common.net.Requester;

public class HttpRequester implements Requester {

    public JsonObject nSendRequest(Request req) {
        if(!(req instanceof HttpRequest)) {
            throw new IllegalArgumentException("Request is not HttpRequest!");
        }
        return null;
        //TODO
    }

}
