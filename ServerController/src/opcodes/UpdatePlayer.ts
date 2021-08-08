import { ErrorNumbers } from '../opcodes'
import { getPlayerByUUID, getPlayerByUsername, updatePlayer } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(!json['data']) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            return;
        }
        var player = null;
        if(json['uuid']) {
            var p1 = getPlayerByUUID(json['uuid']);
            if(p1) {
                player = p1;
            }else {
                if(json['username']) {
                    player = getPlayerByUsername(json['username']);
                }
            }
        }else if(json['username']) {
            player = getPlayerByUsername(json['username']);
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_OR_USERNAME_FIELD});
        }
        if(player) {
            updatePlayer(player, json['data']);
            websocket.respond(json, {ok: true, player});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.PLAYER_NOT_FOUND});
        }
    }
}