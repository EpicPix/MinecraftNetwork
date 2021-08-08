import { getDefaultRank } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        websocket.respond(json, {ok: true, rank: getDefaultRank()});
    }
}