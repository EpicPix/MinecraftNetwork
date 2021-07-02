package ga.epicpix.network.common;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CommonUtils {

    public static String urlEncode(String str) {
        if(Reflection.getMethod(URLEncoder.class, "encode", true, String.class, String.class) != null) {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8.toString());
        }else {
            return (String) Reflection.callMethod(URLEncoder.class, "encode", false, str, StandardCharsets.UTF_8);
        }
    }

}
