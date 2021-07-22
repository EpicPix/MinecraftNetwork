package ga.epicpix.network.common;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;

@Deprecated(forRemoval = true)
public class MongoCredentials {

    public String username;
    public String password;
    public String host;
    public boolean srv;

    public ConnectionString toConnectionString() {
        return new ConnectionString("mongodb" + (srv?"+srv":"") + "://" + CommonUtils.urlEncode(username) + ":" + CommonUtils.urlEncode(password) + "@" + host);
    }

    public static MongoCredentials get() {
        return new Gson().fromJson(Secrets.getSecret("mongodb"), MongoCredentials.class);
    }

}
