import { ErrorNumbers } from '../opcodes'
import { getDefaultRank, getPlayerOrCreate, updatePlayer } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['data']) {
            if(json['uuid']) {
                if(json['username']) {
                    var player = getPlayerOrCreate({uuid: json['uuid'], username: json['username'], rank: getDefaultRank().id, firstLogin: Date.now(), lastLogin: -1});
                    updatePlayer(player, json['data']);
                    websocket.respond(json, {ok: true, player});
                }else {
                    websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_USERNAME_FIELD});
                }
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
        }
    }
}