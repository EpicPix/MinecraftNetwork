package ga.epicpix.network.common;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;

public class CommonUtils {

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {}
        return str;
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

    public static String makeStringLengthPrepend(String string, int length, String c) {
        StringBuilder builder = new StringBuilder(string);
        while(builder.length() <= length) {
            builder.insert(0, c);
        }
        return builder.toString();
    }

    public static String componentsToString(ChatComponent[] components) {
        Boolean bold = null, strikethrough = null, italic = null, obfuscated = null, underlined = null;
        String color = "white";

        StringBuilder generated = new StringBuilder();
        for(ChatComponent component : components) {
            boolean reupdate = false;
            if(component.bold!=null && component.bold!=bold) {
                bold = component.bold;
                reupdate = true;
            }
            if(component.strikethrough!=null && component.strikethrough!=strikethrough) {
                strikethrough = component.strikethrough;
                reupdate = true;
            }
            if(component.italic!=null && component.italic!=italic) {
                italic = component.italic;
                reupdate = true;
            }
            if(component.obfuscated!=null && component.obfuscated!=obfuscated) {
                obfuscated = component.obfuscated;
                reupdate = true;
            }
            if(component.underlined!=null && component.underlined!=underlined) {
                underlined = component.underlined;
                reupdate = true;
            }
            if(component.color!=null && !component.color.equals(color)) {
                color = component.color;
                reupdate = true;
            }

            if(reupdate) {
                generated.append(ChatColor.convertColorText("/" + color + "/"));
                if(bold) generated.append(ChatColor.convertColorText("/bold/"));
                if(strikethrough) generated.append(ChatColor.convertColorText("/strikethrough/"));
                if(italic) generated.append(ChatColor.convertColorText("/italic/"));
                if(obfuscated) generated.append(ChatColor.convertColorText("/obfuscated/"));
                if(underlined) generated.append(ChatColor.convertColorText("/underlined/"));
            }
            generated.append(component.text);
        }

        return generated.toString();
    }

    public static String removeColorCodes(String str) {
        StringBuilder generated = new StringBuilder();
        for(int i = 0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c==ChatColor.COLOR_CHAR) {
                i++;
            }else {
                generated.append(str);
            }
        }
        return generated.toString();
    }

    public static String replaceAt(String str, int at, String to) {
        StringBuilder n = new StringBuilder(str);
        for(int i = 0; i<to.length(); i++) {
            n.setCharAt(i + at, to.charAt(i));
        }
        return n.toString();
    }

    public static String replaceCharactersSpecial(String in, String check, char what, char with) {
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
