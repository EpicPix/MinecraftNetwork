package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.annotations.NonNull;
import ga.epicpix.network.common.net.Request;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.data.RequestData;

public final class WebSocketRequest implements Request {

    private final int opcode;
    private final RequestData data;

    private WebSocketRequest(int opcode, @NonNull RequestData data) {
        this.opcode = opcode;
        this.data = data;
    }

    public int getOpcode() {
        return opcode;
    }

    @CallerSensitive
    public RequestData getData() {
        if(!Reflection.getCaller().equals(WebSocketRequester.class.getName())) {
            throw new SecurityException("Cannot get data of this request!");
        }
        return data;
    }

    public static WebSocketRequest createRequest(@NonNull RequestData data) {
        return new WebSocketRequest(data.getOpcode(), data);
    }

    public static JsonObject sendRequest(@NonNull WebSocketRequest request) {
        return WebSocketRequester.sendRequest(request);
    }

}
