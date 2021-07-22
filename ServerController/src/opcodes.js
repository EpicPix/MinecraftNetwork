//0xxx xxxx xxxx xxxx = client -> server

//1xxx xxxx xxxx xxxx = server -> client, requires action, should be sent only when the type is CLI or a client type that can handle this action
//any other server packet should have a Request ID so that the server can respond to the client without a Packet ID

const StringOpcodes = {
    AUTHENTICATE: 0x0000,
    UPDATE_SERVER_DATA: 0x0001,
    REMOVE_SERVER: 0x0002,

    //SEND_SIGNAL: 0x0003,
    //LIST_SERVERS: 0x0004

    //SERVER_SIGNAL: 0x8000
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
                        if(serv.id === json['server']) {
                            server = serv;
                            break;
                        }
                    }
                    var flags = 0x0000;
                    if(server === null) {
                        flags |= 0x0001;
                        server = { id: json['server'], type: "UNKNOWN", onlinePlayers: -1, maxPlayers: -1, version: {protocol: -1, name: "???"}, details: {ip: "0.0.0.0", port: -1}, bootMillis: -1 };
                        servers.push(server);
                    }
                    var data = json['data'];
                    var copy = function(a, b, c, d) {
                        if(typeof b[c] !== 'undefined') {
                            a[c] = b[c];
                            flags |= d;
                        }
                    }
                    copy(server, data, 'type', 0x0002);
                    copy(server, data, 'onlinePlayers', 0x0004);
                    copy(server, data, 'maxPlayers', 0x0008);
                    copy(server, data, 'version', 0x0010);
                    copy(server, data, 'details', 0x0020);
                    copy(server, data, 'bootMillis', 0x0040);
                    websocket.respond(json, {ok: true, server, flags});
                }else {
                    websocket.respond(json, {error: {id: 2, message: "No data field specified"}});
                }
            }else {
                websocket.respond(json, {error: {id: 1, message: "No server field specified"}});
            }
        }
    },
    handleRemoveServer: function(websocket, json) {
        if(websocket.checkAuth()) {
            //TODO
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
    if((opcode >> 31) == 0) {
        if(!OpcodeHandler[toOpcodeFunctionName(opcode)]) {
            console.error(`Handler for opcode '${toOpcodeName(opcode)}' not found! ${toOpcodeFunctionName(opcode)}()`);
            process.exit(1);
        }
    }
}

module.exports = { StringOpcodes, OpcodeHandler, toOpcodeName, toOpcodeFunctionName };