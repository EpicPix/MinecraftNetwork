import { ErrorNumbers } from '../opcodes'
import { servers } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            var server = null;
            for(var serv of servers) {
                if(serv.public.id === json['server']) {
                    server = serv;
                    break;
                }
            }
            if(server!==null) {
                if(server.websocket!==null) {
                    server.websocket.userData.server = null;
                }
                websocket.userData.server = json['server'];
                server.websocket = websocket;
                websocket.respond(json, {ok: true});
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.SERVER_NOT_FOUND});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}