package ga.epicpix.network.common.websocket.requests;

import ga.epicpix.network.common.ranks.Rank;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.settings.SettingsManager;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.WebSocketConnection;

public final class RequestPolicies {

    //TODO: Permissions for removing servers and updating some values of servers will be a thing
    public static boolean isAllowed(int opcode, String clazz) {
        if((opcode==Opcodes.UPDATE_SERVER_DATA
                || opcode==Opcodes.REMOVE_SERVER
                || opcode==Opcodes.MAKE_WEB_SOCKET_SERVER_OWNER
                || opcode==Opcodes.SEND_SIGNAL
                || opcode==Opcodes.LIST_SERVERS
                || opcode==Opcodes.GET_SERVER) && clazz.equals(ServerInfo.class.getName())) return true;
        if((opcode==Opcodes.GET_SETTING
                || opcode==Opcodes.GET_SETTING_OR_DEFAULT
                || opcode==Opcodes.SET_SETTING
                || opcode==Opcodes.GET_SETTINGS) && clazz.equals(SettingsManager.class.getName())) return true;
        if((opcode==Opcodes.GET_RANK
                || opcode==Opcodes.GET_RANKS
                || opcode==Opcodes.GET_DEFAULT_RANK) && clazz.equals(Rank.class.getName())) return true;
        return clazz.equals(WebSocketConnection.class.getName());
    }

}