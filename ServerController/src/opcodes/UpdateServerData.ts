import { ErrorNumbers } from '../opcodes'
import { servers } from '../index';
import { sendWebhook } from '../webhooks';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            if(json['data']) {
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
                var publicData = server.public;
                var data = json['data'];
                var copy = function(a, b, c, d) {
                    if(typeof b[c] !== 'undefined') {
                        a[c] = b[c];
                        flags |= d;
                    }
                }
                copy(publicData, data, 'type', 0x0002);
                copy(publicData, data, 'onlinePlayers', 0x0004);
                copy(publicData, data, 'maxPlayers', 0x0008);
                copy(publicData, data, 'version', 0x0010);
                copy(publicData, data, 'details', 0x0020);
                copy(publicData, data, 'bootMillis', 0x0040);
                if((flags&0x0001)==0x0001) {
                    sendWebhook('Server Manager', 'Server Created', `The server \`${server.public.id}\` got created`, 0x22E022);
                }
                websocket.respond(json, {ok: true, server: publicData, flags});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DATA_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}