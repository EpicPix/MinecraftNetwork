package ga.epicpix.network.common.websocket.requests;

import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.WebSocketConnection;

public final class RequestPolicies {

    //TODO: Permissions for removing servers and updating some values of servers will be a thing
    public static boolean isAllowed(int opcode, String clazz) {
        if((opcode==Opcodes.UPDATE_SERVER_DATA || opcode==Opcodes.REMOVE_SERVER) && clazz.equals(ServerInfo.class.getName())) {
            return true;
        }
        return clazz.equals(WebSocketConnection.class.getName());
    }

}