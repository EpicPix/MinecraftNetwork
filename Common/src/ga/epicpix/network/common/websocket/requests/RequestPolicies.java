package ga.epicpix.network.common.websocket.requests;

import ga.epicpix.network.common.websocket.WebSocketConnection;

final class RequestPolicies {

    static boolean isAllowed(int opcode, String clazz) {
        return clazz.equals(WebSocketConnection.class.getName());
    }

}