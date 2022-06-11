import { ErrorNumbers } from '../opcodes'

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        websocket.respond(json, {ok: false, errno: ErrorNumbers.NOT_IMPLEMENTED_YET});
        // websocket.respond(json, {ok: true, modules: []});
    }
}