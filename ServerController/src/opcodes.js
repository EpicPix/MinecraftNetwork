//0xxx xxxx xxxx xxxx = client -> server

//1xxx xxxx xxxx xxxx = server -> client, requires action, should be sent only when the type is CLI or a client type that can handle this action
//any other server packet should have a Request ID so that the server can respond to the client without a Packet ID

const StringOpcodes = {
    AUTHENTICATE: 0x00,

    //REGISTER_SERVER: 0x10,
    //UNREGISTER_SERVER: 0x11,
    //SEND_SIGNAL: 0x12,
    //LIST_SERVERS: 0x13
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
                console.log(login)
                console.log(`${login['username']} / ${username}`);
                if(login['username']===username && login['password']===password) {
                    success = true;
                    break;
                }
            }
            websocket.userData.authenticated = success;
            websocket.userData.clientType = json['clientType'];
            websocket.respond(json, {opcode: StringOpcodes.AUTHENTICATION_RESPONSE, success});
            if(!success) websocket.close(4005, 'Authentication failed');
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
    return `handle${name[0]}${name.toLowerCase().slice(1)}`;
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