package ga.epicpix.network.common.net.http;

import ga.epicpix.network.common.net.Request;
import ga.epicpix.network.common.net.Requester;

public class HttpRequester implements Requester {

    public void sendRequest(Requesta req) {
        if(!(req instanceof HttpRequest)) {
            throw new IllegalArgumentException("Request is not HttpRequest!");
        }
        //TODO
    }

}
