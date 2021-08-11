package ga.epicpix.network.common.ranks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.Opcodes;
import ga.epicpix.network.common.net.websocket.requests.WebsocketRequest;
import ga.epicpix.network.common.net.websocket.requests.data.*;

import java.util.ArrayList;

public class RankManager {

    public static Errorable<Rank> getRank(String rank) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_RANK, GetRankRequest.build(rank)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    public static Errorable<ArrayList<Rank>> getRanks() {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_RANKS, GetRanksRequest.build()));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        ArrayList<Rank> ranks = new ArrayList<>();
        for(JsonElement e : obj.getAsJsonArray("ranks")) {
            ranks.add(Rank.rankFromJsonObject((JsonObject) e));
        }
        return new Errorable<>(ranks);
    }

    public static Errorable<Rank> getDefaultRank() {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.GET_DEFAULT_RANK, GetDefaultRankRequest.build()));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    public static Errorable<Rank> updateRank(String rank, UpdateRankRequest.Data update) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.RANK_UPDATE, UpdateRankRequest.build(rank, update)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    public static Errorable<Rank> createRank(String rank, UpdateRankRequest.Data data) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.CREATE_RANK, CreateRankRequest.build(rank, data)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    public static Errorable<Boolean> deleteRank(String rank) {
        JsonObject obj = WebsocketRequest.sendRequest(WebsocketRequest.createRequest(Opcodes.DELETE_RANK, DeleteRankRequest.build(rank)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

}
