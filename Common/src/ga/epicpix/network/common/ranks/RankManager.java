package ga.epicpix.network.common.ranks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.websocket.Errorable;
import ga.epicpix.network.common.websocket.Opcodes;
import ga.epicpix.network.common.websocket.requests.Request;
import ga.epicpix.network.common.websocket.requests.data.GetDefaultRankRequest;
import ga.epicpix.network.common.websocket.requests.data.GetRankRequest;
import ga.epicpix.network.common.websocket.requests.data.GetRanksRequest;

import java.util.ArrayList;

public class RankManager {

    public static Errorable<Rank> getRank(String rank) {
        JsonObject obj = Request.sendRequest(Request.createRequest(Opcodes.GET_RANK, GetRankRequest.build(rank)));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    public static Errorable<ArrayList<Rank>> getRanks() {
        JsonObject obj = Request.sendRequest(Request.createRequest(Opcodes.GET_RANKS, GetRanksRequest.build()));
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
        JsonObject obj = Request.sendRequest(Request.createRequest(Opcodes.GET_DEFAULT_RANK, GetDefaultRankRequest.build()));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

}
