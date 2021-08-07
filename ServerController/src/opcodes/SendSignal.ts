import { Capabilities, ErrorNumbers, StringOpcodes } from '../opcodes'

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            if(json['signal']) {
                const servers = require('../index').servers;
                var server = null;
                for(var serv of servers) {
                    if(serv.public.id === json['server']) {
                        server = serv;
                        break;
                    }
                }
                if(server!==null) {
                    if(server.websocket!==null) {
                        if(server.websocket.hasCapability(Capabilities.CAPSRVSIG)) {
                            server.websocket.send(JSON.stringify({opcode: StringOpcodes.SERVER_SIGNAL, signal: json.signal}));
                            websocket.respond(json, {ok: true});
                        }else {
                            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_CAP});
                        }
                    }else {
                        websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_WEBSOCKET});
                    }
                }else {
                    websocket.respond(json, {ok: false, errno: ErrorNumbers.SERVER_NOT_FOUND});
                }
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}