import { ErrorNumbers } from '../opcodes'
import {getModuleInfo, saveModule} from "../modules";

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['module']) {
            const moduleBytes = Buffer.from(json['module'], 'base64');
            const moduleInfo = getModuleInfo(moduleBytes);
            if(moduleInfo === null) {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.INVALID_DATA});
                return;
            }
            if(!/^[a-zA-Z0-9_-]{2,128}$/.test(moduleInfo.id)) {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.INVALID_MODULE_INFO});
                return;
            }
            if(!/^v?[0-9]+(\.[0-9]+)*$/.test(moduleInfo.version)) {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.INVALID_MODULE_INFO});
                return;
            }
            if(!Array.isArray(moduleInfo.permissions)) {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.INVALID_MODULE_INFO});
                return;
            }
            saveModule(moduleBytes, moduleInfo.id, moduleInfo.version);
            websocket.respond(json, {ok: true, module: moduleInfo});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_MODULE_FIELD});
        }
    }
}