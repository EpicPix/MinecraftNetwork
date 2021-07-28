package ga.epicpix.network.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Secrets {

    private static Properties secrets;

    private static void loadSecrets() {
        try {
            InputStream in = Secrets.class.getResourceAsStream("/secrets.properties");
            if(in==null) {
                throw new FileNotFoundException("Secrets file not found! (secrsts.properties)");
            }
            if(secrets==null) {
                secrets = new Properties();
            }else {
                secrets.clear();
            }
            secrets.load(in);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSecret(String secret) {
        if(secrets==null) loadSecrets();
        if(secret.startsWith("serverController.") && !Reflection.getCaller().equals("ga.epicpix.network.common.websocket.WebSocketCredentials")) {
            throw new IllegalCallerException("Tried to access Server Controller secrets!");
        }
        return (String) secrets.get(secret);
    }

}
