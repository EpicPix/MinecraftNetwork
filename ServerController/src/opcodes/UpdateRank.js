const { Capabilities, ErrorNumbers, StringOpcodes } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['rank']) {
            if(json['data']) {
                const ranks = require('../index').ranks;
                var rank = null;
                for(var rk of ranks) {
                    if(rk.id === json['rank']) {
                        rank = rk;
                        break;
                    }
                }
                if(server === null) {
                    websocket.respond(json, {ok: false, errno: ErrorNumbers.RANK_NOT_FOUND});
                    return;
                }
                var flags = 0x0000;
                var data = json['data'];
                var copy = function(a, b, c, d) {
                    if(typeof b[c] !== 'undefined') {
                        a[c] = b[c];
                        flags |= d;
                    }
                }
                copy(rank, data, 'priority', 0x0001);
                copy(rank, data, 'prefix', 0x0002);
                copy(rank, data, 'suffix', 0x0004);
                copy(rank, data, 'permissions', 0x008);
                copy(rank, data, 'nameColor', 0x0010);
                copy(rank, data, 'chatColor', 0x0020);
                for(const ws of require('../index').wss.clients) {
                    if(ws.hasCapability(Capabilities.CAPRANKUPD)) {
                        ws.send(JSON.stringify({opcode: StringOpcodes.RANK_UPDATE, rank}));
                    }
                }
                websocket.respond(json, {ok: true, rank, flags});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_RANK_FIELD});
        }
    }
}