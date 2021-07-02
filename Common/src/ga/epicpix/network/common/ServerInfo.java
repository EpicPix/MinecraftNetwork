package ga.epicpix.network.common;

public class ServerInfo {

    public enum ServerType {
        UNKNOWN
    }

    public String id;
    public ServerType type;
    public int maxPlayers;

    public String toString() {
        return "ServerInfo{id=" + (id==null?"null":("'" + id + "'")) + ", type=" + type + ", maxPlayers=" + maxPlayers + "}";
    }
}
