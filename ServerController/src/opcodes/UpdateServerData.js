const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            if(json['data']) {
                const servers = require('../index').servers;
                var server = null;
                for(var serv of servers) {
                    if(serv.public.id === json['server']) {
                        server = serv;
                        break;
                    }
                }
                var flags = 0x0000;
                if(server === null) {
                    flags |= 0x0001;
                    server = { public: { id: json['server'], type: "UNKNOWN", onlinePlayers: -1, maxPlayers: -1, version: {protocol: -1, name: "???"}, details: {ip: "0.0.0.0", port: 0}, bootMillis: -1 }, websocket: null };
                    servers.push(server);
                }
                var public = server.public;
                var data = json['data'];
                var copy = function(a, b, c, d) {
                    if(typeof b[c] !== 'undefined') {
                        a[c] = b[c];
                        flags |= d;
                    }
                }
                copy(public, data, 'type', 0x0002);
                copy(public, data, 'onlinePlayers', 0x0004);
                copy(public, data, 'maxPlayers', 0x0008);
                copy(public, data, 'version', 0x0010);
                copy(public, data, 'details', 0x0020);
                copy(public, data, 'bootMillis', 0x0040);
                websocket.respond(json, {ok: true, server: public, flags});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}