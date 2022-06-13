package ga.epicpix.network.common.ranks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ga.epicpix.network.common.annotations.ChecksPermission;
import ga.epicpix.network.common.modules.ModulePermission;
import ga.epicpix.network.common.net.websocket.Errorable;
import ga.epicpix.network.common.net.websocket.WebSocketRequester;
import ga.epicpix.network.common.net.websocket.requests.*;

import java.util.ArrayList;

public final class RankManager {

    @ChecksPermission(ModulePermission.GET_RANK)
    public static Errorable<Rank> getRank(String rank) {
        JsonObject obj = WebSocketRequester.sendRequest(GetRankRequest.build(rank));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    @ChecksPermission(ModulePermission.GET_RANKS)
    public static Errorable<ArrayList<Rank>> getRanks() {
        JsonObject obj = WebSocketRequester.sendRequest(GetRanksRequest.build());
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        ArrayList<Rank> ranks = new ArrayList<>();
        for(JsonElement e : obj.getAsJsonArray("ranks")) {
            ranks.add(Rank.rankFromJsonObject(e));
        }
        return new Errorable<>(ranks);
    }

    @ChecksPermission(ModulePermission.GET_DEFAULT_RANK)
    public static Errorable<Rank> getDefaultRank() {
        JsonObject obj = WebSocketRequester.sendRequest(GetDefaultRankRequest.build());
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    @ChecksPermission(ModulePermission.UPDATE_RANK)
    public static Errorable<Rank> updateRank(String rank, UpdateRankRequest.Data update) {
        JsonObject obj = WebSocketRequester.sendRequest(UpdateRankRequest.build(rank, update));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    @ChecksPermission(ModulePermission.CREATE_RANK)
    public static Errorable<Rank> createRank(String rank, UpdateRankRequest.Data data) {
        JsonObject obj = WebSocketRequester.sendRequest(CreateRankRequest.build(rank, data));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(Rank.rankFromJsonObject(obj.getAsJsonObject("rank")));
    }

    @ChecksPermission(ModulePermission.DELETE_RANK)
    public static Errorable<Boolean> deleteRank(String rank) {
        JsonObject obj = WebSocketRequester.sendRequest(DeleteRankRequest.build(rank));
        if(!obj.get("ok").getAsBoolean()) {
            return new Errorable<>(obj.get("errno").getAsInt());
        }
        return new Errorable<>(true);
    }

}
