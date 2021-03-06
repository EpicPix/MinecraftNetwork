package ga.epicpix.network.common.net.websocket;

public final class Opcodes {

    public static final int AUTHENTICATE = 0x0000;

    public static final int UPDATE_SERVER_DATA = 0x0001;
    public static final int REMOVE_SERVER = 0x0002;
    public static final int MAKE_WEB_SOCKET_SERVER_OWNER = 0x0003;
    public static final int SEND_SIGNAL = 0x0004;
    public static final int LIST_SERVERS = 0x0005;
    public static final int GET_SERVER = 0x0006;
    public static final int GET_VERSIONS = 0x0007;

    public static final int GET_SETTING = 0x0010;
    public static final int GET_SETTING_OR_DEFAULT = 0x0011;
    public static final int SET_SETTING = 0x0012;
    public static final int GET_SETTINGS = 0x0013;

    public static final int GET_RANK = 0x0020;
    public static final int GET_RANKS = 0x0021;
    public static final int GET_DEFAULT_RANK = 0x0022;
    public static final int UPDATE_RANK = 0x0023;
    public static final int CREATE_RANK = 0x0024;
    public static final int DELETE_RANK = 0x0025;

    public static final int GET_PLAYER = 0x0030;
    public static final int UPDATE_PLAYER = 0x0031;
    public static final int UPDATE_PLAYER_OR_CREATE = 0x0032;
    public static final int GET_PLAYER_OR_CREATE = 0x0033;

    public static final int GET_MODULE = 0x0040;
    public static final int GET_MODULES = 0x0041;
    public static final int ADD_MODULE = 0x0042;

    public static final int SERVER_SIGNAL = 0x8000;
    public static final int SETTINGS_UPDATE = 0x8001;
    public static final int RANK_UPDATE = 0x8002;

}
