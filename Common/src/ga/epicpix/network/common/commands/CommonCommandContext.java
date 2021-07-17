package ga.epicpix.network.common.commands;

public class CommonCommandContext extends AbstractCommandContext<CommandSender, CommandPlayer> {

    public CommonCommandContext(String commandName) {
        super(commandName);
    }

    public boolean isPlayer() {
        return getSender() instanceof CommandPlayer;
    }

    public boolean isConsole() {
        return getSender() instanceof CommandConsole;
    }

    public void sendMessage(String message) {
        getSender().sendMessage(message);
    }

    public void run() {

    }

}
