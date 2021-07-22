package ga.epicpix.network.common.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.ReturnableRunnable;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.AuthenticateRequest;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public final class WebSocketConnection implements WebSocket.Listener {

    private static ClientType clientType = ClientType.OTHER;

    private static WebSocket webSocket;
    private static WebSocketConnection connection;
    private static boolean connected = false;

    private static long nextRequestId = 0;

    private static final ArrayList<RequestFuture> futures = new ArrayList<>();
    private static final HashMap<String, ReturnableRunnable> hooks = new HashMap<>();

    public static void addHook(String hook, ReturnableRunnable run) {
        hooks.remove(hook);
        hooks.put(hook, run);
    }

    public static void setClientType(ClientType clientType) {
        if(clientType==null) {
            throw new NullPointerException("ClientType is null!");
        }
        if(WebSocketConnection.clientType==ClientType.OTHER) {
            WebSocketConnection.clientType = clientType;
        }
    }

    public static ClientType getClientType() {
        return clientType;
    }

    public static void connect() {
        if(!connected) {
            connectWithCredentials(WebSocketCredentials.get());
        }
    }

    private static void connectWithCredentials(WebSocketCredentials creds) {
        connection = new WebSocketConnection();
        try {
            webSocket = HttpClient.newBuilder().build().newWebSocketBuilder().buildAsync(creds.toURI(), connection).join();
            connected = true;
            if(!connection.sendAuthenticateRequest(creds)) {
                if(hooks.containsKey("type_auth")) {
                    var objauth = hooks.get("type_auth").run();
                    if(objauth instanceof String[] auth) {
                        if(auth.length<2) {
                            System.err.println("The auth array is not 2 elements");
                        }else {
                            connectWithCredentials(WebSocketCredentials.set(creds, auth[0], auth[1]));
                        }
                    }else {
                        System.err.println("Return type is not String[]!");
                    }
                }
            }
        } catch(CompletionException e) {
            connection = null;
            System.err.println("Could not connect");
        }
    }

    public static JsonObject sendRequest(Request req) {
        if(!Reflection.getCaller().equals(Request.class.getName())) {
            throw new SecurityException("Use the Request class to call sendRequest");
        }
        return connection.sendRequest(req.getData().toJson(), req.getOpcode());
    }

    private boolean sendAuthenticateRequest(WebSocketCredentials credentials) {
        return Request.sendRequest(Request.createRequest(Opcodes.AUTHENTICATE, AuthenticateRequest.build(credentials.username(), credentials.password(), clientType))).get("success").getAsBoolean();
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

        if(statusCode!=4005) {
            System.out.println("WebSocket closed: " + statusCode + " for the reason" + reason);
        }
        return null;
    }

    public void onError(WebSocket ws, Throwable error) {

    }


}

