package ga.epicpix.network.common.commands;

import ga.epicpix.network.common.players.PlayerInfo;

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

    @SuppressWarnings("unchecked")
    public final Player getPlayer() {
        return (Player) sender;
    }

    public final String getCommandName() {
        return commandName;
    }

    public void sendUsage() {
        sendMessage("No usage defined");
    }

    public final PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public abstract boolean isPlayer();
    public abstract boolean isConsole();
    public abstract void sendMessage(String message);

    public abstract void run();

}
