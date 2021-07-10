package ga.epicpix.network.common;

import static ga.epicpix.network.common.Language.LanguageEntry;

public class DefaultLanguage {

    public static final Language ENGLISH = new Language()
            .setId("ENGLISH")
            .addEntry(new LanguageEntry("error.no_permissions", "/red/You don't have enough permissions!"))
            .addEntry(new LanguageEntry("kick.update.bungeecord", "An update occurred"))
            .addEntry(new LanguageEntry("error.command.only_player", "/red/This command can only be run by players!"))
            .addEntry(new LanguageEntry("command.usage.server", "/red/Usage: /server <server>"))
            .addEntry(new LanguageEntry("error.server.unknown", "/red/Unknown server!"))
            .addEntry(new LanguageEntry("error.server.connect", "/red/Could not connect to this server!"))
            .addEntry(new LanguageEntry("error.server.connecting", "/red/Already trying to connect to this server!"))
            .addEntry(new LanguageEntry("error.server.already_connected", "/red/Already connected to this server!"));

}
