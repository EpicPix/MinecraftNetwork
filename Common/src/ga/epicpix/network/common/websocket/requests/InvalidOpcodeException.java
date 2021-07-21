package ga.epicpix.network.common.websocket.requests;

public class InvalidOpcodeException extends IllegalArgumentException {

    public InvalidOpcodeException() {}

    public InvalidOpcodeException(String s) {
        super(s);
    }
}
