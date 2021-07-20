package ga.epicpix.network.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Secrets {

    private static JsonObject secrets;

    private static void loadSecrets() {
        try {
            InputStream in = Secrets.class.getResourceAsStream("/secrets.json");
            if(in==null) {
                throw new FileNotFoundException("Secrets file not found! (secrsts.json)");
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = in.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            secrets = new Gson().fromJson(result.toString(StandardCharsets.UTF_8), JsonObject.class);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonElement getSecret(String secret) {
        if(secrets==null) loadSecrets();
        if(secret.equalsIgnoreCase("serverController") && !Reflection.getCaller().equals("ga.epicpix.network.common.websocket.WebSocketCredentials")) {
            throw new IllegalCallerException("Tried to access Server Controller Login Credentials secrets!");
        }
        return secrets.get(secret);
    }

}
