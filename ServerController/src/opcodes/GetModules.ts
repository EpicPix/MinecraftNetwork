import {getModules} from "../modules";

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        websocket.respond(json, {ok: true, modules: getModules().map(x => x[2])});
    }
}