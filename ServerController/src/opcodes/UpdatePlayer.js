const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(!json['data']) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            return;
        }
        var player = null;
        if(json['uuid']) {
            var p1 = require('../index').getPlayerByUUID(json['uuid']);
            if(p1) {
                player = pl;
            }else {
                if(json['username']) {
                    player = require('../index').getPlayerByUsername(json['username']);
                }
            }
        }else if(json['username']) {
            player = require('../index').getPlayerByUsername(json['username']);
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_OR_USERNAME_FIELD});
        }
        if(player) {
            require('../index').updatePlayer(player, json['data']);
            websocket.respond(json, {ok: true, player});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.PLAYER_NOT_FOUND});
        }
    }
}