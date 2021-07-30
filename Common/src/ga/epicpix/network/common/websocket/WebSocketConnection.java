package ga.epicpix.network.common.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.AuthenticateRequest;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public final class WebSocketConnection implements WebSocket.Listener {

    private static ClientType clientType = ClientType.OTHER;
    private static int capabilities = 0;

    private static WebSocket webSocket;
    static WebSocketConnection connection;
    static Requester requester;

    private static boolean connected = false;

    private static final HashMap<String, Supplier<Object>> hooks = new HashMap<>();

    private static ServerHook signalHandler = null;
    private static ServerHook settingsChangedHandler = null;
    private static ServerHook rankChangedHandler = null;

    private static final ServerHook serverHook = (opcode, data, requester) -> {
        ArrayList<Capability> caps = new ArrayList<>();
        for(Capability c : Capability.values()) {
            if((capabilities&c.getBits())==c.getBits()) {
                caps.add(c);
            }
        }
        if(opcode == Opcodes.SERVER_SIGNAL && caps.contains(Capability.CAPSRVSIG)) {
            if(signalHandler!=null) signalHandler.handle(opcode, data, requester);
        }else if(opcode == Opcodes.SETTINGS_UPDATE && caps.contains(Capability.CAPSETTINGUPD)) {
            if(settingsChangedHandler!=null) settingsChangedHandler.handle(opcode, data, requester);
        }else if(opcode == Opcodes.RANK_UPDATE && caps.contains(Capability.CAPRANKUPD)) {
            if(rankChangedHandler!=null) rankChangedHandler.handle(opcode, data, requester);
        }
    };

    public static void makeCapable(Capability capability) {
        if(capability==null) {
            throw new NullPointerException("Capability is null");
        }
        if(connected) {
            throw new IllegalStateException("Tried to make client capable while already connected");
        }
        capabilities |= capability.getBits();
    }

    public static void addHook(String hook, Supplier<Object> run) {
        hooks.remove(hook);
        hooks.put(hook, run);
    }

    public static void setSignalHandler(ServerHook hook) {
        if(signalHandler ==null) {
            makeCapable(Capability.CAPSRVSIG);
            signalHandler = hook;
        }else {
            throw new IllegalStateException("Tried to set SignalHandler when it's already set");
        }
    }

    public static void setSettingsUpdateHandler(ServerHook hook) {
        if(settingsChangedHandler ==null) {
            makeCapable(Capability.CAPSETTINGUPD);
            settingsChangedHandler = hook;
        }else {
            throw new IllegalStateException("Tried to set SettingsChangedHandler when it's already set");
        }
    }

    public static void setRankUpdateHandler(ServerHook hook) {
        if(rankChangedHandler ==null) {
            makeCapable(Capability.CAPRANKUPD);
            rankChangedHandler = hook;
        }else {
            throw new IllegalStateException("Tried to set RankChangedHandler when it's already set");
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
                    var objauth = hooks.get("type_auth").get();
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
        return Request.sendRequest(Request.createRequest(Opcodes.AUTHENTICATE, AuthenticateRequest.build(credentials.username(), credentials.password(), getClientType(), capabilities))).get("success").getAsBoolean();
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
                        new Thread(() -> serverHook.handle(opcode, data, requester)).start();
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

