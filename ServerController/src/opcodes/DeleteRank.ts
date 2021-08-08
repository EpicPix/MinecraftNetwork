import { ErrorNumbers } from '../opcodes'
import { ranks } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['rank']) {
            var rank = null;
            for(var rk of ranks) {
                if(rk.id === json['rank']) {
                    rank = rk;
                    break;
                }
            }
            if(rank === null) {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.RANK_NOT_FOUND});
                return;
            }
            ranks.filter(ele => ele != rank);
            websocket.respond(json, {ok: true});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_RANK_FIELD});
        }
    }
}