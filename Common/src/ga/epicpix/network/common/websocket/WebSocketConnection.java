package ga.epicpix.network.common.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.MongoCredentials;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;

public final class WebSocketConnection implements WebSocket.Listener {

    private static WebSocket webSocket;
    private static WebSocketConnection connection;
    private static boolean connected = false;

    private static long nextRequestId = 0;

    private static final ArrayList<RequestFuture> futures = new ArrayList<>();

    public static void connect() {
        if(!connected) {
            connection = new WebSocketConnection();
            WebSocketCredentials creds = WebSocketCredentials.get();
            webSocket = HttpClient.newBuilder().build().newWebSocketBuilder().buildAsync(creds.toURI(), connection).join();
            connected = true;
            if(!connection.sendAuthenticateRequest(creds)) {
                System.err.println("Could not authenticate!");
            }
        }
    }

    private boolean sendAuthenticateRequest(WebSocketCredentials credentials) {
        JsonObject req = new JsonObject();
//        System.out.println(sendRequest(req, Opcodes.AUTHENTICATE));
        return true;
    }

    private JsonObject sendRequest(JsonObject request, int opcode) {
        return sendRequestAsync(request, opcode).join();
    }

    private RequestFuture sendRequestAsync(JsonObject request, int opcode) {
        nextRequestId++;
        request.addProperty("opcode", opcode);
        request.addProperty("rid", nextRequestId);
        RequestFuture future = new RequestFuture(nextRequestId);
        futures.add(future);
        send(request.toString());
        return future;
    }

    private void send(String data) {
        webSocket.sendText(data, true).join();
    }

    public void onOpen(WebSocket ws) {
        ws.request(1);
    }

    public CompletionStage<?> onText(WebSocket ws, CharSequence str, boolean last) {
        JsonObject data = new Gson().fromJson(str.toString(), JsonObject.class);
        if(data.has("rid")) {
            for(RequestFuture future : futures) {
                if(future.rid()==data.get("rid").getAsLong()) {
                    future.complete(data);
                    break;
                }
            }
        }
        ws.request(1);
        return null;
    }

    public CompletionStage<?> onBinary(WebSocket ws, ByteBuffer data, boolean last) {
        ws.request(1);
        return null;
    }

    public CompletionStage<?> onPing(WebSocket ws, ByteBuffer message) {
        ws.request(1);
        return null;
    }

    public CompletionStage<?> onPong(WebSocket ws, ByteBuffer message) {
        ws.request(1);
        return null;
    }

    public CompletionStage<?> onClose(WebSocket ws, int statusCode, String reason) {
        connected = false;
        futures.forEach(x -> x.cancel(true));
        futures.clear();
        nextRequestId = 0;
        connection = null;
        ws = null;
        return null;
    }

    public void onError(WebSocket ws, Throwable error) {

    }


}

