package ga.epicpix.network.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Secrets {

    private static JsonObject secrets;

    private static void loadSecrets() {
        try {
            InputStream in = Secrets.class.getResourceAsStream("/secrets.json");
            if(in==null) {
                System.err.println("Failed to get secrets.json");
                return;
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = in.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
            secrets = new Gson().fromJson(result.toString(StandardCharsets.UTF_8), JsonObject.class);
        }catch(Exception e) {
            System.err.println("Failed to load the secrets!");
        }
    }

    public static JsonElement getSecret(String secret) {
        if(secrets==null) loadSecrets();
        return secrets.get(secret);
    }

}
