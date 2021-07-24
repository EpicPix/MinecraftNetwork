const { ErrorNumbers } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['setting']) {
            const settings = require('../index').settings;
            var setting = null;
            for(var st of settings) {
                if(st.name === json['setting']) {
                    setting = st;
                    break;
                }
            }
            if(setting) {
                websocket.respond(json, {ok: true, setting: setting.value});
            }else {
                websocket.respond(json, {ok: false});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SETTING_FIELD});
        }
    }
}