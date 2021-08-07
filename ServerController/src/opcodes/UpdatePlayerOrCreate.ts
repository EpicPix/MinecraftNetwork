import { ErrorNumbers } from '../opcodes'

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['data']) {
            if(json['uuid']) {
                if(json['username']) {
                    var player = require('../index').getPlayerOrCreate({uuid: json['uuid'], username: json['username'], rank: require('../index').getDefaultRank().id, firstLogin: Date.now(), lastLogin: -1});
                    require('../index').updatePlayer(player, json['data']);
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