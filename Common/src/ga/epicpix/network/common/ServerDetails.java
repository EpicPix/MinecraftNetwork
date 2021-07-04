package ga.epicpix.network.common;

public class ServerDetails {

    public String ip;
    public int port;

    public String toString() {
        return "ServerDetails{ip=" + CommonUtils.toString(ip) + ", port=" + port + "}";
    }
}
