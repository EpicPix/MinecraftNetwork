//0xxx xxxx xxxx xxxx = client -> server

//1xxx xxxx xxxx xxxx = server -> client, requires action, should be sent only when the type is CLI or a client type that can handle this action
//any other server packet should have a Request ID so that the server can respond to the client without a Packet ID

const StringOpcodes = {
    AUTHENTICATE: 0x0000,
    UPDATE_SERVER_DATA: 0x0001,
    REMOVE_SERVER: 0x0002,
    MAKE_WEB_SOCKET_SERVER_OWNER: 0x0003,
    SEND_SIGNAL: 0x0004,
    LIST_SERVERS: 0x0005,
    GET_SERVER: 0x0006,

    SERVER_SIGNAL: 0x8000
}

const ErrorNumbers = {
    NO_SERVER_FIELD: 0x01,
    NO_DATA_FIELD: 0x02,
    NO_SIGNAL_FIELD: 0x03,

    SERVER_NOT_FOUND: 0x20,
    NO_SERVER_WEBSOCKET: 0x21
}


const OpcodeHandler = {
    handleAuthenticate: function(websocket, json) {
        if(!websocket.userData.authenticated) {
            var username = json['username'];
            var password = json['password'];
            if(typeof username === undefined || username === null) {
                websocket.close(4003, 'Username not provided');
                return;
            }
            if(typeof password === undefined || password === null) {
                websocket.close(4004, 'Password not provided');
                return;
            }
            const logins = require('./index').logins;
            var success = logins.length===0;
            for(const login of logins) {
                if(login['username']===username && login['password']===password) {
                    success = true;
                    break;
                }
            }
            websocket.userData.authenticated = success;
            websocket.userData.clientType = json['clientType'];
            websocket.respond(json, {success});
            if(!success) websocket.close(4005, 'Authentication failed');
        }
    },
    handleUpdateServerData: function(websocket, json) {
        if(websocket.checkAuth()) {
            if(json['server']) {
                if(json['data']) {
                    const servers = require('./index').servers;
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
    },
    handleRemoveServer: function(websocket, json) {
        if(websocket.checkAuth()) {
            if(json['server']) {
                const servers = require('./index').servers;
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
    },
    handleMakeWebSocketServerOwner: function(websocket, json) {
        if(websocket.checkAuth()) {
            if(json['server']) {
                const servers = require('./index').servers;
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
    },
    handleSendSignal: function(websocket, json) {
        if(websocket.checkAuth()) {
            if(json['server']) {
                if(json['signal']) {
                    const servers = require('./index').servers;
                    var server = null;
                    for(var serv of servers) {
                        if(serv.public.id === json['server']) {
                            server = serv;
                            break;
                        }
                    }
                    if(server!==null) {
                        if(server.websocket!==null) {
                            server.websocket.send(JSON.stringify({opcode: StringOpcodes.SERVER_SIGNAL, signal: json.signal}));
                            websocket.respond(json, {ok: true});
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
    },
    handleListServers: function(websocket, json) {
        if(websocket.checkAuth()) {
            var safeServers = [];
            require('./index').servers.forEach((server) => {
                safeServers.push(server.public);
            });
            websocket.respond(json, {ok: true, servers: safeServers});
        }
    },
    handleGetServer: function(websocket, json) {
        if(websocket.checkAuth()) {
            if(json['server']) {
                const servers = require('./index').servers;
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
}




Object.freeze(StringOpcodes);

function toOpcodeName(opcodev) {
    for(var [ opcode, opcodeval ] of Object.entries(StringOpcodes)) {
        if(opcodeval===opcodev) {
            return opcode;
        }
    }
    return null;
}

function toOpcodeFunctionName(opcode) {
    var name = toOpcodeName(opcode);
    if(name==null) return null;
    var words = name.toLowerCase().split('_');
    for(var i = 0; i < words.length; i++) {
        words[i] = `${words[i][0].toUpperCase()}${words[i].slice(1)}`;
    }
    return `handle${words.join('')}`;
}

for(var opcode of Object.values(StringOpcodes)) {
    if((opcode >> 15) == 0) {
        if(!OpcodeHandler[toOpcodeFunctionName(opcode)]) {
            console.error(`Handler for opcode '${toOpcodeName(opcode)}' not found! ${toOpcodeFunctionName(opcode)}()`);
            process.exit(1);
        }
    }
}

module.exports = { StringOpcodes, OpcodeHandler, toOpcodeName, toOpcodeFunctionName };