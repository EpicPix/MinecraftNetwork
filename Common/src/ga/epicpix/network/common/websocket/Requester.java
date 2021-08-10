package ga.epicpix.network.common.websocket;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.websocket.requests.Request;

import java.util.ArrayList;

public final class Requester {

    long nextRequestId = 0;
    final ArrayList<RequestFuture> futures = new ArrayList<>();

    public Requester(WebSocketConnection connection) {
        if(connection==null) {
            throw new IllegalArgumentException("WebSocketConnection is null");
        }
    }

    @CallerSensitive
    public static JsonObject sendRequest(Request req) {
        if(!Reflection.getCaller().equals(Request.class.getName())) {
            throw new SecurityException("Use the Request class to call sendRequest");
        }
        JsonObject resp = WebSocketConnection.requester.sendRequest(req.getData().toJson(), req.getOpcode());
        resp.remove("rid");
        return resp;
    }

    public JsonObject sendRequest(JsonObject request, int opcode) {
        return sendRequestAsync(request, opcode).join();
    }

    public RequestFuture sendRequestAsync(JsonObject request, int opcode) {
        nextRequestId++;
        request.addProperty("opcode", opcode);
        request.addProperty("rid", nextRequestId);
        RequestFuture future = new RequestFuture(nextRequestId);
        futures.add(future);
        WebSocketConnection.connection.send(request.toString());
        return future;
    }

}
