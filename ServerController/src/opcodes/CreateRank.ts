import { Capabilities, ErrorNumbers, StringOpcodes } from '../opcodes'
import { ranks } from '../index';
import { ClientWebSocket, wss } from '../websocket';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['rank']) {
            if(json['data']) {
                var rank = null;
                for(var rk of ranks) {
                    if(rk.id === json['rank']) {
                        rank = rk;
                        break;
                    }
                }
                if(rank !== null) {
                    websocket.respond(json, {ok: false, errno: ErrorNumbers.RANK_ALREADY_EXISTS});
                    return;
                }
                rank = {priority: 10, prefix: [], suffix: [], permissions: [], nameColor: "white", chatColor: "white"};
                ranks.push(rank);
                var data = json['data'];
                var copy = function(a, b, c) {
                    if(typeof b[c] !== 'undefined') {
                        a[c] = b[c];
                    }
                }
                copy(rank, data, 'priority');
                copy(rank, data, 'prefix');
                copy(rank, data, 'suffix');
                copy(rank, data, 'permissions');
                copy(rank, data, 'nameColor');
                copy(rank, data, 'chatColor');
                for(const ws of wss.clients as Set<ClientWebSocket>) {
                    if(ws.hasCapability(Capabilities.CAPRANKUPD)) {
                        ws.send(JSON.stringify({opcode: StringOpcodes.RANK_UPDATE, rank}));
                    }
                }
                websocket.respond(json, {ok: true, rank});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_RANK_FIELD});
        }
    }
}