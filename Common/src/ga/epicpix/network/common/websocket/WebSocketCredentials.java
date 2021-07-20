package ga.epicpix.network.common.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.Secrets;

import java.net.URI;

record WebSocketCredentials(boolean secure, String host, int port, String username, String password) {

    static WebSocketCredentials get() {
        JsonObject obj = new Gson().fromJson(Secrets.getSecret("serverController"), JsonObject.class);
        return new WebSocketCredentials(obj.get("secure").getAsBoolean(), obj.get("host").getAsString(), obj.get("port").getAsInt(), obj.get("username").getAsString(), obj.get("password").getAsString());
    }

    URI toURI() {
        return URI.create((secure?"wss":"ws") + "://" + host + ":" + port);
    }

}
