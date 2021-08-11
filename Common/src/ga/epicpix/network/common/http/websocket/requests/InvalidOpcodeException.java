package ga.epicpix.network.common.http.websocket.requests;

public class InvalidOpcodeException extends IllegalArgumentException {

    public InvalidOpcodeException() {}

    public InvalidOpcodeException(String s) {
        super(s);
    }
}
