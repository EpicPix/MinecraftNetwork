package ga.epicpix.network.cli;

import ga.epicpix.network.common.CommonUtils;
import ga.epicpix.network.common.Mongo;
import ga.epicpix.network.common.servers.ServerInfo;
import ga.epicpix.network.common.servers.ServerSignal;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ga.epicpix.network.common.CommonUtils.*;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Network Manager CLI");

        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        Logger.getLogger("org.bson").setLevel(Level.SEVERE);

        System.out.println("Type \"help\" for help");

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String line = sc.nextLine();
            String[] cmd = line.split(" ");
            String cmdline = getParam(cmd, 0);
            if(cmdline.equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }else if(cmdline.equals("help")) {
                System.out.println("exit - Exits this manager");
                System.out.println("help - Show help");
                System.out.println("server <id> stop - Stop a server");
                System.out.println("servers - List servers");
            }else if(cmdline.equals("server")) {
                if(getParam(cmd, 1) != null) {
                    ServerInfo server = CommonUtils.getServerInfo(getParam(cmd, 1));
                    if(server==null) {
                        System.out.println("Unknown server! Use \"servers\" to list servers");
                    }else {
                        if(getParam(cmd, 2) != null && getParam(cmd, 2).equals("stop")) {
                            server.sendSignal(ServerSignal.STOP);
                            System.out.println("Signal sent");
                        }else {
                            System.out.println("Usage: server " + server.id + " <stop>");
                        }
                    }
                }else {
                    System.out.println("Wrong usage! Use \"help\" for help");
                }
            }else if(cmdline.equals("servers")) {
                ArrayList<Document> documents = CommonUtils.iteratorToList(Mongo.getCollection("data", "servers").find().iterator());
                ArrayList<ServerInfo> servers = CommonUtils.documentsToObjects(documents, ServerInfo.class);
                showServerListing(servers);
            }else {
                System.out.println("Unknown command. Type \"help\" for help");
            }
        }
    }

    public static void showServerListing(ArrayList<ServerInfo> servers) {
        int id = 2;
        for(ServerInfo server : servers) if(id < server.id.length()) id = server.id.length();

        int type = 4;
        for(ServerInfo server : servers) if(type < server.type.length()) type = server.type.length();

        int players = 7;
        for(ServerInfo server : servers) if(players < (server.onlinePlayers + "/" + server.maxPlayers).length()) players = (server.onlinePlayers + "/" + server.maxPlayers).length();

        int version = 7;
        for(ServerInfo server : servers) if(version < server.version.getName().length()) version = server.version.getName().length();

        int ip = 2;
        for(ServerInfo server : servers) if(ip < (server.details.ip + ":" + server.details.port).length()) ip = (server.details.ip + ":" + server.details.port).length();

        id += 2;
        type += 2;
        players += 2;
        version += 2;
        ip += 2;

        String rep = "║" + repeat(" ", id) + "|" + repeat(" ", type) + "|" + repeat(" ", players) + "|" + repeat(" ", version) + "|" + repeat(" ", ip) + "║";

        if(servers.size()==0) {
            System.out.println("╔" + repeat("═", rep.length()-2) + "╗");
            System.out.println("No servers found.");
            System.out.println(repeat("═", rep.length()));
            return;
        }

        rep = replaceAt(rep, 1+(id-1)/2, "ID");
        rep = replaceAt(rep, 1+id+1+(type-1)/2-1, "TYPE");
        rep = replaceAt(rep, 1+id+1+type+1+(players-1)/2-3, "PLAYERS");
        rep = replaceAt(rep, 1+id+1+type+1+players+1+(version-1)/2-3, "VERSION");
        rep = replaceAt(rep, 1+id+1+type+1+players+1+version+1+(ip-1)/2, "IP");

        System.out.println(replaceCharactersSpecial("╔" + repeat("═", rep.length()-2) + "╗", rep, '|', '╤'));
        System.out.println(rep.replace('|', '│'));
        System.out.println(replaceCharactersSpecial("╟" + repeat("─", rep.length()-2) + "╢", rep, '|', '┼'));

        for(ServerInfo server : servers) {
            String serverOut = "║" + repeat(" ", id) + "│" + repeat(" ", type) + "│" + repeat(" ", players) + "│" + repeat(" ", version) + "│" + repeat(" ", ip) + "║";

            serverOut = replaceAt(serverOut, 2, server.id);
            serverOut = replaceAt(serverOut, 1+id+2, server.type);
            serverOut = replaceAt(serverOut, 1+id+3+type, server.onlinePlayers + "/" + server.maxPlayers);
            serverOut = replaceAt(serverOut, 1+id+4+type+players, server.version.getName());
            serverOut = replaceAt(serverOut, 1+id+5+type+players+version, server.details.ip + ":" + server.details.port);

            System.out.println(serverOut);
        }

        System.out.println(replaceCharactersSpecial("╚" + repeat("═", rep.length()-2) + "╝", rep, '|', '╧'));

    }

    public static String getParam(String[] args, int arg) {
        return args.length > arg ? args[arg] : null;
    }

}
