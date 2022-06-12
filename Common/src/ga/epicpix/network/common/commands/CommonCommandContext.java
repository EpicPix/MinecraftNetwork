package ga.epicpix.network.common.commands;

import static ga.epicpix.network.common.text.ChatColor.convertColorText;

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
        getSender().sendMessage(convertColorText(message));
    }

    public void run() {

    }

}
