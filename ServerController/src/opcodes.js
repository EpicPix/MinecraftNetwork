//0xxx xxxx xxxx xxxx = client -> server

//1xxx xxxx xxxx xxxx = server -> client, requires action, should be sent only when the type is CLI or a client type that can handle this action
//any other server packet should have a Request ID so that the server can respond to the client without a Packet ID

const StringOpcodes = {
    //Main 0x0000
    AUTHENTICATE: 0x0000,

    //Servers 0x0001-0x000f
    UPDATE_SERVER_DATA: 0x0001,
    REMOVE_SERVER: 0x0002,
    MAKE_WEB_SOCKET_SERVER_OWNER: 0x0003,
    SEND_SIGNAL: 0x0004,
    LIST_SERVERS: 0x0005,
    GET_SERVER: 0x0006,

    //Settings 0x0010-0x001f
    GET_SETTING: 0x0010,
    GET_SETTING_OR_DEFAULT: 0x0011,
    SET_SETTING: 0x0012,

    //Ranks 0x0020-0x002f
    GET_RANK: 0x0020,
    GET_RANKS: 0x0021,
    GET_DEFAULT_RANK: 0x0022,

    //Languages? 0x0030-0x003f
    //Players? 0x0040-0x004f

    //From Server 0x8000-0xffff

    SERVER_SIGNAL: 0x8000,
    SETTINGS_UPDATE: 0x8001
}

const ErrorNumbers = {
    NO_SERVER_FIELD: 0x01,
    NO_DATA_FIELD: 0x02,
    NO_SIGNAL_FIELD: 0x03,
    NO_SETTING_FIELD: 0x04,

    SERVER_NOT_FOUND: 0x20,
    NO_SERVER_WEBSOCKET: 0x21,
    NO_CAP: 0x22
}

const Capabilities = {
    CAPSRVSIG: 0x0001, //SERVER SIGNAL
    CAPSETTINGUPD: 0x0002 //SETTINGS UPDATE
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

function toOpcodeId(opcode) {
    var name = toOpcodeName(opcode);
    if(name==null) return null;
    var words = name.toLowerCase().split('_');
    for(var i = 0; i < words.length; i++) {
        words[i] = `${words[i][0].toUpperCase()}${words[i].slice(1)}`;
    }
    return `${words.join('')}`;
}

const fs = require('fs');
const path = require('path');

var failed = false;

const dir = path.resolve(__dirname, 'opcodes');

for(var opcode of Object.values(StringOpcodes)) {
    if((opcode >> 15) == 0) {
        if(!fs.existsSync(path.resolve(dir, `${toOpcodeId(opcode)}.js`))) {
            console.error(`Handler for opcode '${toOpcodeName(opcode)}' not found! opcodes/${toOpcodeId(opcode)}.js`);
            failed = true;
        }
    }
}
if(failed) {
    process.exit(1);
}

module.exports = { StringOpcodes, ErrorNumbers, Capabilities, toOpcodeName, toOpcodeId };