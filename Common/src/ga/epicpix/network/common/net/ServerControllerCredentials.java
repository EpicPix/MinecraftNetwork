package ga.epicpix.network.common.net;

import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.Secrets;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.net.http.HttpConnection;
import ga.epicpix.network.common.net.websocket.WebSocketConnection;

import java.net.URI;

public class ServerControllerCredentials {

    private final String url, username, password;

    private ServerControllerCredentials(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static ServerControllerCredentials set(ServerControllerCredentials existing, String username, String password) {
        return new ServerControllerCredentials(existing.url, username, password);
    }

    @CallerSensitive
    public static ServerControllerCredentials get() {
        var caller = Reflection.getCaller();
        if(caller != WebSocketConnection.class && caller != HttpConnection.class) {
            if(caller == null) throw new SecurityException("Cannot get credentials");
            throw new SecurityException("Cannot get credentials from " + caller);
        }
        String url = Secrets.getSecret("serverController.url");
        String username = Secrets.getSecret("serverController.username");
        String password = Secrets.getSecret("serverController.password");
        return new ServerControllerCredentials(url, username, password);
    }

    public URI toURI() {
        return URI.create(url);
    }

}
