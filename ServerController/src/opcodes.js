//0xxx xxxx = client -> server
//1xxx xxxx = server -> client

const StringOpcodes = {
    AUTHENTICATE: 0x00,

    AUTHENTICATION_RESPONSE: 0x80
}


const OpcodeHandler = {
    handleAuthenticate: function(websocket, json) {
        if(!websocket.userData.authenticated) {
            websocket.respond(json, {opcode: StringOpcodes.AUTHENTICATION_RESPONSE, test: 123});
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
    if((opcode >> 7) == 0) {
        if(!OpcodeHandler[toOpcodeFunctionName(opcode)]) {
            console.error(`Handler for opcode '${toOpcodeName(opcode)}' not found! ${toOpcodeFunctionName(opcode)}()`);
            process.exit(1);
        }
    }
}

module.exports = { StringOpcodes, OpcodeHandler, toOpcodeName, toOpcodeFunctionName };