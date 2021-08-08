import { ErrorNumbers } from '../opcodes'
import { getPlayerByUUID, getPlayerByUsername } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['uuid']) {
            var p1 = getPlayerByUUID(json['uuid']);
            if(p1) {
                websocket.respond(json, {ok: true, player: p1});
                return;
            }
            if(json['username']) {
                var p2 = getPlayerByUsername(json['username']);
                if(p2) {
                    websocket.respond(json, {ok: true, player: p2});
                    return;
                }
            }
            websocket.respond(json, {ok: false, errno: ErrorNumbers.PLAYER_NOT_FOUND});
        }else if(json['username']) {
            var p1 = getPlayerByUsername(json['username']);
            if(p1) {
                websocket.respond(json, {ok: true, player: p1});
                return;
            }
            websocket.respond(json, {ok: false, errno: ErrorNumbers.PLAYER_NOT_FOUND});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_OR_USERNAME_FIELD});
        }
    }
}