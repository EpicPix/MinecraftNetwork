package ga.epicpix.network.common.websocket;

public class Errorable<T> {

    private final int errorCode;
    private final T value;

    public Errorable(int errorCode) {
        this(errorCode, null);
    }

    public Errorable(T value) {
        this(0, value);
    }

    public Errorable(int errorCode, T value) {
        this.errorCode = errorCode;
        this.value = value;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean hasFailed() {
        return errorCode!=0;
    }

    public T getValue() {
        if(value==null) {
            throw new NullPointerException("Value is null");
        }
        return value;
    }

}
