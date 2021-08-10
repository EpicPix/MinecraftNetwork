package ga.epicpix.network.common.websocket.requests;

import com.google.gson.JsonObject;
import ga.epicpix.network.common.Reflection;
import ga.epicpix.network.common.annotations.CallerSensitive;
import ga.epicpix.network.common.annotations.NonNull;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.Requester;
import ga.epicpix.network.common.websocket.WebSocketConnection;
import ga.epicpix.network.common.websocket.requests.data.RequestData;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Request {

    private final int opcode;
    private final RequestData data;

    private Request(int opcode, @NonNull RequestData data) {
        this.opcode = opcode;
        this.data = data;
    }

    public int getOpcode() {
        return opcode;
    }

    @CallerSensitive
    public RequestData getData() {
        if(!Reflection.getCaller().equals(Requester.class.getName())) {
            throw new SecurityException("Cannot get data of this request!");
        }
        return data;
    }

    @CallerSensitive
    public static Request createRequest(int opcode, @NonNull RequestData data) {
        String opcodename = null;
        try {
            boolean okopcode = false;
            for(Field opcodes : Opcodes.class.getDeclaredFields()) {
                if(opcodes.getModifiers() == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) {
                    if(opcodes.getInt(null) == opcode) {
                        opcodename = opcodes.getName();
                        okopcode = true;
                        break;
                    }
                }
            }
            if(!okopcode) {
                throw new InvalidOpcodeException("Invalid opcode: " + opcode);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(!RequestPolicies.isAllowed(opcode, Reflection.getCaller())) {
            throw new SecurityException(Reflection.getCaller() + " is not allowed to use opcode " + opcodename);
        }
        if(data==null) {
            throw new NullPointerException("RequestData is null");
        }
        if(data.getOpcode()!=opcode) {
            throw new IllegalStateException("The RequestData opcode is not the opcode provided!");
        }
        return new Request(opcode, data);
    }

    public static JsonObject sendRequest(@NonNull Request request) {
        return Requester.sendRequest(request);
    }

}
