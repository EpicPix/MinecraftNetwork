package ga.epicpix.network.common;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;

public class CommonUtils {

    public static String urlEncode(String str) {
        if(Reflection.getMethod(URLEncoder.class, "encode", true, String.class, String.class) != null) {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8.toString());
        }else {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8);
        }
    }

    public static String toString(String string) {
        return string==null?"null":("'" + string + "'");
    }

    public static ArrayList<InetAddress> possibleAddresses() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            ArrayList<InetAddress> addresses = new ArrayList<>();
            while (e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = ee.nextElement();
                    if (i instanceof Inet4Address) {
                        if (!i.isLoopbackAddress()) {
                            addresses.add(i);
                        }
                    }
                }
            }
            return addresses;
        } catch (SocketException e) {
            System.err.println("Failed to get possible addresses");
        }
        return new ArrayList<>();
    }

    public static InetAddress possibleAddress() {
        ArrayList<InetAddress> addresses = possibleAddresses();
        if(addresses.size()!=0) {
            return addresses.get(0);
        }
        return InetAddress.getLoopbackAddress();
    }

}
