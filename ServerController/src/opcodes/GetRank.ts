import { ErrorNumbers } from '../opcodes'
import { ranks } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['rank']) {
            var gotrank = null;
            for(var rank of ranks) {
                if(rank.id===json['rank']) {
                    gotrank = rank;
                    break;
                }
            }
            if(gotrank) {
                websocket.respond(json, {ok: true, rank});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.RANK_NOT_FOUND});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_RANK_FIELD});
        }
    }
}