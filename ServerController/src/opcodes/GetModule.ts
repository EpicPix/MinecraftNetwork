import { ErrorNumbers } from '../opcodes'
import {getModule} from "../modules";

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(!json['id']) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_ID_FIELD});
            return;
        }
        if(!json['version']) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_VERSION_FIELD});
            return;
        }
        if(!/[a-zA-Z0-9_-]{2,128}/.test(json['id'])) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_ID_FIELD});
            return;
        }
        if(!/^v?[0-9]+(\.[0-9]+)*$/.test(json['version'])) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_VERSION_FIELD});
            return;
        }

        let module = getModule(json['id'], json['version']);
        if(module === null) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.MODULE_NOT_FOUND});
            return;
        }
        websocket.respond(json, {ok: true, info: module[1], module: module[0].toString("base64")});
    }
}