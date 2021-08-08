import { ranks } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(ranks.length===0) {
            ranks.push({id: "default", priority: 0, prefix: [], suffix: [], permissions: [], nameColor: "white", chatColor: "white"});
        }
        
        websocket.respond(json, {ok: true, ranks});
    }
}