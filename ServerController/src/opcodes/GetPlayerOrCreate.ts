import { ErrorNumbers } from '../opcodes'
import { getPlayerOrCreate, getDefaultRank } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['uuid']) {
            if(json['username']) {
                websocket.respond(json, {ok: true, player: getPlayerOrCreate({uuid: json['uuid'], username: json['username'], rank: getDefaultRank().id, firstLogin: Date.now(), lastLogin: -1})});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_USERNAME_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_FIELD});
        }
    }
}