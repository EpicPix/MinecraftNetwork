package ga.epicpix.network.common;

public class ChatComponent {

    public static final String TYPE = "CHAT_COMPONENT";

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
}
