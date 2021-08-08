import { servers } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        var safeServers = [];
        servers.forEach((server) => {
            safeServers.push(server.public);
        });
        websocket.respond(json, {ok: true, servers: safeServers});
    }
}