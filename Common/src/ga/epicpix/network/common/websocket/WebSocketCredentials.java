package ga.epicpix.network.common.websocket;

import ga.epicpix.network.common.Secrets;

import java.net.URI;

record WebSocketCredentials(String url, String username, String password) {

    static WebSocketCredentials set(WebSocketCredentials existing, String username, String password) {
        return new WebSocketCredentials(existing.url, username, password);
    }

    static WebSocketCredentials get() {
        String url = Secrets.getSecret("serverController.url");
        String username = Secrets.getSecret("serverController.username");
        String password = Secrets.getSecret("serverController.password");
        return new WebSocketCredentials(url, username, password);
    }

    URI toURI() {
        return URI.create(url);
    }

}
