package ga.epicpix.network.common;

import com.google.gson.Gson;

public class MongoCredentials {

    public String username;
    public String password;
    public String host;
    public boolean srv;

    public String toConnectionString() {
        return "mongodb" + (srv?"-srv":"") + "://" + username + ":" + password + "@" + host;
    }

    public static MongoCredentials get() {
        return new Gson().fromJson(Secrets.getSecret("mongodb"), MongoCredentials.class);
    }

}
