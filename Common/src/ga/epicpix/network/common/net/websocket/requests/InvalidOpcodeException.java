package ga.epicpix.network.common.net.websocket.requests;

public class InvalidOpcodeException extends IllegalArgumentException {

    public InvalidOpcodeException() {}

    public InvalidOpcodeException(String s) {
        super(s);
    }
}
