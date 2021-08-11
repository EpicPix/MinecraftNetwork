package ga.epicpix.network.common.http.websocket;

import com.google.gson.JsonObject;

import java.util.concurrent.CompletableFuture;

public class RequestFuture extends CompletableFuture<JsonObject> {

    private final long rid;

    RequestFuture(long rid) {
        this.rid = rid;
    }

    long rid() {
        return rid;
    }

}
