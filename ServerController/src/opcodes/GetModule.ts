import { ErrorNumbers } from '../opcodes'

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['id']) {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NOT_IMPLEMENTED_YET});   
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_ID_FIELD});
        }
    }
}