const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            const servers = require('../index').servers;
            for(var serv of servers) {
                if(serv.id === json['server']) {
                    const index = servers.indexOf(serv);
                    if (index > -1) {
                        servers.splice(index, 1);
                    }
                    break;
                }
            }
            websocket.respond(json, {ok: true});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}