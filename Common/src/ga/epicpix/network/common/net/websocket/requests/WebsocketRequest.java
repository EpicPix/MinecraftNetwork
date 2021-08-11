package ga.epicpix.network.common.net.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.annotations.NonNull;
import ga.epicpix.network.common.net.Request;
import ga.epicpix.network.common.net.websocket.Requester;
import ga.epicpix.network.common.net.websocket.requests.data.RequestData;

public final class WebsocketRequest implements Request {

    private final int opcode;
    private final RequestData data;

    private WebsocketRequest(int opcode, @NonNull RequestData data) {
        this.opcode = opcode;
        this.data = data;
    }

    public int getOpcode() {
        return opcode;
    }

    @CallerSensitive
    public RequestData getData() {
        if(!Reflection.getCaller().equals(Requester.class.getName())) {
            throw new SecurityException("Cannot get data of this request!");
        }
        return data;
    }

    public static WebsocketRequest createRequest(@NonNull RequestData data) {
        return new WebsocketRequest(data.getOpcode(), data);
    }

    public static JsonObject sendRequest(@NonNull WebsocketRequest request) {
        return Requester.sendRequest(request);
    }

}
