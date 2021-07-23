package ga.epicpix.network.common.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.ReturnableRunnable;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.AuthenticateRequest;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

public final class WebSocketConnection implements WebSocket.Listener {

    private static ClientType clientType = ClientType.OTHER;

    private static WebSocket webSocket;
    static WebSocketConnection connection;
    static Requester requester;

    private static boolean connected = false;

    private static final HashMap<String, ReturnableRunnable> hooks = new HashMap<>();
    private static ServerHook serverHook = null;

    public static void addHook(String hook, ReturnableRunnable run) {
        hooks.remove(hook);
        hooks.put(hook, run);
    }

    public static void setServerHook(ServerHook hook) {
        if(serverHook==null) {
            serverHook = hook;
        }else {
            throw new IllegalStateException("Tried to set ServerHook when it's already set");
        }
    }

    public static void setClientType(ClientType clientType) {
        if(clientType==null) {
            throw new NullPointerException("ClientType is null!");
        }
        if(WebSocketConnection.getClientType()==ClientType.OTHER) {
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
        requester = new Requester(connection);
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

    private boolean sendAuthenticateRequest(WebSocketCredentials credentials) {
        return Request.sendRequest(Request.createRequest(Opcodes.AUTHENTICATE, AuthenticateRequest.build(credentials.username(), credentials.password(), getClientType()))).get("success").getAsBoolean();
    }

    void send(String data) {
        webSocket.sendText(data, true).join();
    }

    public void onOpen(WebSocket ws) {
        ws.request(1);
    }

    public CompletionStage<?> onText(WebSocket ws, CharSequence str, boolean last) {
        JsonObject data = new Gson().fromJson(str.toString(), JsonObject.class);
        if(data.has("rid")) {
            for(RequestFuture future : requester.futures) {
                if(future.rid()==data.get("rid").getAsLong()) {
                    future.complete(data);
                    break;
                }
            }
        }else {
            if(data.has("opcode")) {
                int opcode = data.get("opcode").getAsInt();
                if((opcode & 0x8000) == 0x8000) {
                    if(serverHook!=null) {
                        serverHook.handle(opcode, data, requester);
                    }else {
                        System.err.println("Server Opcode Handler is not set, not handling opcode " + opcode);
                    }
                }else {
                    System.err.println("Not a server handle opcode: " + opcode);
                }
            }else {
                System.err.println("Unknown message: " + data);
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
        requester.futures.forEach(x -> x.cancel(true));
        requester.futures.clear();
        requester.nextRequestId = 0;
        connection = null;
        webSocket = null;

        if(statusCode!=4005) {
            System.out.println("WebSocket closed: " + statusCode + " for the reason" + reason);
        }
        return null;
    }

    public void onError(WebSocket ws, Throwable error) {

    }


}

