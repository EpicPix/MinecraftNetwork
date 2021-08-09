package ga.epicpix.network.common.websocket.requests;

import ga.epicpix.network.common.players.PlayerManager;
import ga.epicpix.network.common.ranks.RankManager;
import ga.epicpix.network.common.servers.ServerManager;
import ga.epicpix.network.common.servers.ServerVersion;
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
                || opcode==Opcodes.GET_SERVER) && clazz.equals(ServerManager.class.getName())) return true;
        if(opcode==Opcodes.GET_VERSIONS && clazz.equals(ServerVersion.class.getName())) return true;
        if((opcode==Opcodes.GET_SETTING
                || opcode==Opcodes.GET_SETTING_OR_DEFAULT
                || opcode==Opcodes.SET_SETTING
                || opcode==Opcodes.GET_SETTINGS) && clazz.equals(SettingsManager.class.getName())) return true;
        if((opcode==Opcodes.GET_RANK
                || opcode==Opcodes.GET_RANKS
                || opcode==Opcodes.GET_DEFAULT_RANK
                || opcode==Opcodes.UPDATE_RANK
                || opcode==Opcodes.CREATE_RANK
                || opcode==Opcodes.DELETE_RANK) && clazz.equals(RankManager.class.getName())) return true;
        if((opcode==Opcodes.GET_PLAYER
                || opcode==Opcodes.UPDATE_PLAYER
                || opcode==Opcodes.UPDATE_PLAYER_OR_CREATE
                || opcode==Opcodes.GET_PLAYER_OR_CREATE) && clazz.equals(PlayerManager.class.getName())) return true;
        return clazz.equals(WebSocketConnection.class.getName());
    }

}