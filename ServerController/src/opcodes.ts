//0xxx xxxx xxxx xxxx = client -> server

//1xxx xxxx xxxx xxxx = server -> client, requires action, should be sent only when the type is CLI or a client type that can handle this action
//any other server packet should have a Request ID so that the server can respond to the client without a Packet ID

export enum StringOpcodes {
    //Main 0x0000
    AUTHENTICATE = 0x0000,

    //Servers 0x0001-0x000f
    UPDATE_SERVER_DATA = 0x0001,
    REMOVE_SERVER = 0x0002,
    MAKE_WEB_SOCKET_SERVER_OWNER = 0x0003,
    SEND_SIGNAL = 0x0004,
    LIST_SERVERS = 0x0005,
    GET_SERVER = 0x0006,
    GET_VERSIONS = 0x0007,

    //Settings 0x0010-0x001f
    GET_SETTING = 0x0010,
    GET_SETTING_OR_DEFAULT = 0x0011,
    SET_SETTING = 0x0012,
    GET_SETTINGS = 0x0013,

    //Ranks 0x0020-0x002f
    GET_RANK = 0x0020,
    GET_RANKS = 0x0021,
    GET_DEFAULT_RANK = 0x0022,
    UPDATE_RANK = 0x0023,
    CREATE_RANK = 0x0024,
    DELETE_RANK = 0x0025,

    //Players? 0x0030-0x003f
    GET_PLAYER = 0x0030,
    UPDATE_PLAYER = 0x0031,
    UPDATE_PLAYER_OR_CREATE = 0x0032,
    GET_PLAYER_OR_CREATE = 0x0033,

    GET_MODULE = 0x0040,
    GET_MODULES = 0x0041,
    ADD_MODULE = 0x0042,

    //From Server 0x8000-0xffff

    SERVER_SIGNAL = 0x8000,
    SETTINGS_UPDATE = 0x8001,
    RANK_UPDATE = 0x8002
}

export enum ErrorNumbers {
    NO_SERVER_FIELD = 0x01,
    NO_DATA_FIELD = 0x02,
    NO_SIGNAL_FIELD = 0x03,
    NO_SETTING_FIELD = 0x04,
    NO_RANK_FIELD = 0x05,
    NO_DEFAULT_FIELD = 0x06,
    NO_UUID_FIELD = 0x07,
    NO_USERNAME_FIELD = 0x08,
    NO_UUID_OR_USERNAME_FIELD = 0x09,
    NO_ID_FIELD = 0x0A,
    NO_MODULE_FIELD = 0x0B,
    NO_VERSION_FIELD = 0x0C,

    SERVER_NOT_FOUND = 0x20,
    NO_SERVER_WEBSOCKET = 0x21,
    NO_CAP = 0x22,
    RANK_NOT_FOUND = 0x23,
    PLAYER_ALREADY_EXISTS = 0x24,
    PLAYER_NOT_FOUND = 0x25,
    INVALID_DATA = 0x26,
    INVALID_MODULE_INFO = 0x27,
    MODULE_NOT_FOUND = 0x28,

    RANK_ALREADY_EXISTS = 0x40,

    NOT_IMPLEMENTED_YET = 0xfffd,
    UNSUPPORTED_OPCODE = 0xfffe,
    INTERNAL_ERROR = 0xffff
}

export enum Capabilities {
    CAPSRVSIG = 0x0001, //SERVER SIGNAL
    CAPSETTINGUPD = 0x0002, //SETTINGS UPDATE
    CAPRANKUPD = 0x0004 //RANK UPDATE
}

export function toOpcodeName(opcodev) {
    for(var [ opcode, opcodeval ] of Object.entries(StringOpcodes)) {
        if(opcodeval===opcodev) {
            return opcode;
        }
    }
    return null;
}

export function toOpcodeId(opcode) {
    var name = toOpcodeName(opcode);
    if(name==null) return null;
    var words = name.toLowerCase().split('_');
    for(var i = 0; i < words.length; i++) {
        words[i] = `${words[i][0].toUpperCase()}${words[i].slice(1)}`;
    }
    return `${words.join('')}`;
}

import fs from 'fs';
import path from 'path';

var failed = false;

const dir = path.resolve(__dirname, 'opcodes');

for(var [, opcodeId ] of Object.entries(StringOpcodes)) {
    if(typeof opcodeId === 'number') {
        if((opcodeId >> 15) == 0) {
            if(!fs.existsSync(path.resolve(dir, `${toOpcodeId(opcodeId)}.js`))) {
                console.error(`Handler for opcode '${toOpcodeName(opcodeId)}' not found! opcodes/${toOpcodeId(opcodeId)}.js`);
                failed = true;
            }
        }
    }
}
if(failed) {
    process.exit(1);
}