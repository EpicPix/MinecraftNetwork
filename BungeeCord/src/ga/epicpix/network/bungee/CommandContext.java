package ga.epicpix.network.bungee;

import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Language;
import ga.epicpix.network.common.PlayerInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

public abstract class CommandContext {

    private boolean dataSet = false;
    private CommandSender sender;
    private PlayerInfo playerInfo;
    private String[] arguments;

    private final String commandName;

    public CommandContext(String commandName) {
        this.commandName = commandName;
    }

    public final CommandContext setData(CommandSender sender, PlayerInfo playerInfo, String[] arguments) {
        if(!dataSet) {
            this.sender = sender;
            this.playerInfo = playerInfo;
            this.arguments = arguments;
            dataSet = true;
        }
        return this;
    }

    public final CommandSender getSender() {
        return sender;
    }

    public final String[] getArguments() {
        return arguments;
    }

    public final boolean isPlayer() {
        return sender instanceof ProxiedPlayer;
    }

    public final ProxiedPlayer getPlayer() {
        return (ProxiedPlayer) sender;
    }

    public final boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    public final void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public final void sendUsage() {
        sendTranslated("command.usage." + commandName);
    }

    public final void sendTranslated(String message) {
        if(isPlayer()) {
            sendMessage(Language.getTranslation(message, getPlayerInfo().language));
        }else {
            sendMessage(CommonUtils.getDefaultLanguage().getTranslation(message));
        }
    }

    public final PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public abstract void run();
}
