package ga.epicpix.network.common.websocket;

public class Opcodes {

    public static final int AUTHENTICATE = 0x0000;
    public static final int UPDATE_SERVER_DATA = 0x0001;
    public static final int REMOVE_SERVER = 0x0002;
    public static final int MAKE_WEB_SOCKET_SERVER_OWNER = 0x0003;
    public static final int SEND_SIGNAL = 0x0004;
    public static final int LIST_SERVERS = 0x0005;
    public static final int GET_SERVER = 0x0006;

    public static final int SERVER_SIGNAL = 0x8000;

}
