package ga.epicpix.network.common;

import static ga.epicpix.network.common.Language.LanguageEntry;

public class DefaultLanguage {

    public static final Language ENGLISH = new Language()
            .setId("ENGLISH")
            .addEntry(new LanguageEntry("error.no_permissions", "/red/You don't have enough permissions!"));

}
