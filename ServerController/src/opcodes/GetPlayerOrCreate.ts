import { ErrorNumbers } from '../opcodes'

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['uuid']) {
            if(json['username']) {
                websocket.respond(json, {ok: true, player: require('../index').getPlayerOrCreate({uuid: json['uuid'], username: json['username'], rank: require('../index').getDefaultRank().id, firstLogin: Date.now(), lastLogin: -1})});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_USERNAME_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_UUID_FIELD});
        }
    }
}