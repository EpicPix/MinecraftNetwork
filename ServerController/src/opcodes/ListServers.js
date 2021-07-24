module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        var safeServers = [];
        require('../index').servers.forEach((server) => {
            safeServers.push(server.public);
        });
        websocket.respond(json, {ok: true, servers: safeServers});
    }
}