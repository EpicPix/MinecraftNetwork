import { versionList } from '../version';

export function run(websocket, json) {
    if(websocket.checkAuth()) {       
        websocket.respond(json, {ok: true, versions: versionList});
    }
}