package ga.epicpix.network.common.commands;

import ga.epicpix.network.common.CommonUtils;

public abstract class CommandSender {

    public abstract String getName();

    public void sendMessage(String message) {
        System.out.println(CommonUtils.removeColorCodes(message));
    }

}
