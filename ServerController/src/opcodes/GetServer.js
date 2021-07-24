const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            const servers = require('../index').servers;
            var server = null;
            for(var serv of servers) {
                if(serv.public.id === json['server']) {
                    server = serv;
                    break;
                }
            }
            websocket.respond(json, {ok: true, server: server.public});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}