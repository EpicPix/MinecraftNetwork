package ga.epicpix.network.cli;

import ga.epicpix.network.common.text.ChatColor;

public class Ansi {

    public static final String DARK_AQUA = "\u001b[36m";
    public static final String AQUA = "\u001b[36;1m";
    public static final String GREEN = "\u001b[32;1m";
    public static final String RED = "\u001b[31;1m";
    public static final String YELLOW = "\u001b[33m";
    public static final String WHITE = "\u001b[37;1m";
    public static final String GRAY = "\u001b[30;1m";
    public static final String LIGHT_GRAY = "\u001b[37m";
    public static final String RESET = "\u001b[0m";

    public static boolean ansi = System.getenv().get("TERM")!=null;

    public static int ansiLength(String str) {
        if(!ansi) return 0;
        return str.length();
    }

    public static String ansi(String in, boolean reset) {
        StringBuilder rebuilt = new StringBuilder();
        in = ChatColor.convertColorText(in);
        if(ansi) {
            for(int i = 0; i<in.length(); i++) {
                if(in.charAt(i)==ChatColor.COLOR_CHAR) {
                    i++;
                    char c = in.charAt(i);
                    if(c=='3') {
                        rebuilt.append(DARK_AQUA);
                    }else if(c=='7') {
                        rebuilt.append(LIGHT_GRAY);
                    }else if(c=='8') {
                        rebuilt.append(GRAY);
                    }else if(c=='a') {
                        rebuilt.append(GREEN);
                    }else if(c=='b') {
                        rebuilt.append(AQUA);
                    }else if(c=='c') {
                        rebuilt.append(RED);
                    }else if(c=='e') {
                        rebuilt.append(YELLOW);
                    }else if(c=='f') {
                        rebuilt.append(WHITE);
                    }else if(c=='r') {
                        rebuilt.append(RESET);
                    }else {
                        rebuilt.append(ChatColor.COLOR_CHAR + c);
                    }
                }else {
                    rebuilt.append(in.charAt(i));
                }
            }
            if(reset) rebuilt.append(RESET);
        }else {
            for(int i = 0; i<in.length(); i++) {
                if(in.charAt(i)==ChatColor.COLOR_CHAR) {
                    i++;
                }else {
                    rebuilt.append(in.charAt(i));
                }
            }
        }
        return rebuilt.toString();
    }

    public static void printlnGreen(String str) {
        System.out.println(GREEN + str);
    }

}
