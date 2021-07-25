module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        websocket.respond(json, {ok: true, rank: require('../index').getDefaultRank()});
    }
}