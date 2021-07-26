package ga.epicpix.network.common.text;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ChatComponent {

    public String text;
    public String color;
    public Boolean bold;
    public Boolean strikethrough;
    public Boolean italic;
    public Boolean obfuscated;
    public Boolean underlined;

    public ChatComponent setText(String text) {
        this.text = text;
        return this;
    }

    public ChatComponent setColor(String color) {
        this.color = color;
        return this;
    }

    public ChatComponent setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public ChatComponent setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public ChatComponent setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public ChatComponent setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public ChatComponent setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public static ChatComponent getFromJsonObject(JsonObject obj) {
        return new Gson().fromJson(obj, ChatComponent.class);
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
                if(bold==Boolean.TRUE) generated.append(ChatColor.convertColorText("/bold/"));
                if(strikethrough==Boolean.TRUE) generated.append(ChatColor.convertColorText("/strikethrough/"));
                if(italic==Boolean.TRUE) generated.append(ChatColor.convertColorText("/italic/"));
                if(obfuscated==Boolean.TRUE) generated.append(ChatColor.convertColorText("/obfuscated/"));
                if(underlined==Boolean.TRUE) generated.append(ChatColor.convertColorText("/underlined/"));
            }
            generated.append(component.text);
        }

        return generated.toString();
    }

}
