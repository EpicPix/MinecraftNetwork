package ga.epicpix.network.common.text;

public class ChatColor {

    public static final char COLOR_CHAR = '§';

    public static String convertColorText(String text) {
        return text
                .replace("/black/", COLOR_CHAR + "0")
                .replace("/dark_blue/", COLOR_CHAR + "1")
                .replace("/dark_green/", COLOR_CHAR + "2")
                .replace("/dark_aqua/", COLOR_CHAR + "3")
                .replace("/dark_red/", COLOR_CHAR + "4")
                .replace("/dark_purple/", COLOR_CHAR + "5")
                .replace("/gold/", COLOR_CHAR + "6")
                .replace("/gray/", COLOR_CHAR + "7")
                .replace("/dark_gray/", COLOR_CHAR + "8")
                .replace("/blue/", COLOR_CHAR + "9")
                .replace("/green/", COLOR_CHAR + "a")
                .replace("/aqua/", COLOR_CHAR + "b")
                .replace("/red/", COLOR_CHAR + "c")
                .replace("/pink/", COLOR_CHAR + "d")
                .replace("/light_purple/", COLOR_CHAR + "d")
                .replace("/yellow/", COLOR_CHAR + "e")
                .replace("/white/", COLOR_CHAR + "f")
                .replace("/obfuscated/", COLOR_CHAR + "k")
                .replace("/bold/", COLOR_CHAR + "l")
                .replace("/strikethrough/", COLOR_CHAR + "m")
                .replace("/underline/", COLOR_CHAR + "n")
                .replace("/italic/", COLOR_CHAR + "o")
                .replace("/reset/", COLOR_CHAR + "r")
                ;
    }

    public static String removeColorCodes(String str) {
        StringBuilder generated = new StringBuilder();
        for(int i = 0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c==ChatColor.COLOR_CHAR) {
                i++;
            }else {
                generated.append(c);
            }
        }
        return generated.toString();
    }

}
