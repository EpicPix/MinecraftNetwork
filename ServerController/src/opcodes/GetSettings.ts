module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        const settings = require('../index').settings;
        websocket.respond(json, {ok: true, settings});
    }
}