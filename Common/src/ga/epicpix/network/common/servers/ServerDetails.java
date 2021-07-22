package ga.epicpix.network.common.servers;

import ga.epicpix.network.common.CommonUtils;

public record ServerDetails(String ip, int port) {

    public ServerDetails {
        if(ip==null) {
            throw new IllegalArgumentException("IP cannot be null!");
        }
        if(port < 0) {
            throw new IllegalArgumentException("Port must be positive");
        }
    }

    public String toString() {
        return "ServerDetails{" +
                "ip=" + CommonUtils.toString(ip) +
                ", port=" + port +
                '}';
    }
}