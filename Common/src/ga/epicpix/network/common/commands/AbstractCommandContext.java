package ga.epicpix.network.common.commands;

import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Language;
import ga.epicpix.network.common.PlayerInfo;

public abstract class AbstractCommandContext<Sender, Player extends Sender> {

    private boolean dataSet = false;
    private Sender sender;
    private PlayerInfo playerInfo;
    private String[] arguments;

    private final String commandName;

    public AbstractCommandContext(String commandName) {
        this.commandName = commandName;
    }

    public final AbstractCommandContext<Sender, Player> setData(Sender sender, PlayerInfo playerInfo, String[] arguments) {
        if(!dataSet) {
            this.sender = sender;
            this.playerInfo = playerInfo;
            this.arguments = arguments;
            dataSet = true;
        }
        return this;
    }

    public final Sender getSender() {
        return sender;
    }

    public final String[] getArguments() {
        return arguments;
    }

    public final Player getPlayer() {
        return (Player) sender;
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

    public abstract boolean isPlayer();
    public abstract boolean isConsole();
    public abstract void sendMessage(String message);

    public abstract void run();

}
