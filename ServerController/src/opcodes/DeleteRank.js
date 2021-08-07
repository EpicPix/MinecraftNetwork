const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['rank']) {
            const ranks = require('../index').ranks;
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