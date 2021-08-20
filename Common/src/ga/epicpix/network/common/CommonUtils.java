package ga.epicpix.network.common;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public final class CommonUtils {

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
        if(!addresses.isEmpty()) {
            return addresses.get(0);
        }
        return InetAddress.getLoopbackAddress();
    }

    public static String makeStringLengthPrepend(String string, int length, String c) {
        StringBuilder builder = new StringBuilder(string);
        while(builder.length() <= length) {
            builder.insert(0, c);
        }
        return builder.toString();
    }

    public static String replaceAt(String str, int at, CharSequence to) {
        StringBuilder n = new StringBuilder(str);
        for(int i = 0; i<to.length(); i++) {
            n.setCharAt(i + at, to.charAt(i));
        }
        return n.toString();
    }

    public static String replaceCharactersSpecial(String in, CharSequence check, char what, char with) {
        int amt = Math.min(in.length(), check.length());
        int left = Math.max(0, in.length()-check.length());
        StringBuilder gen = new StringBuilder();
        for(int i = 0; i<amt; i++) {
            if(check.charAt(i)==what) {
                gen.append(with);
            }else {
                gen.append(in.charAt(i));
            }
        }
        gen.append(in.substring(in.length() - left));
        return gen.toString();
    }

}
