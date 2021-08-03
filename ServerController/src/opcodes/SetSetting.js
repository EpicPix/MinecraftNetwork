const { Capabilities, ErrorNumbers, StringOpcodes } = require('../opcodes');

module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['setting']) {
            if(json['value']) {
                const settings = require('../index').settings;
                var setting = null;
                for(var st of settings) {
                    if(st.name === json['setting']) {
                        setting = st;
                        break;
                    }
                }
                if(!setting) {
                    setting = {name: json['setting']};
                    settings.push(setting);
                }
                setting.value = json['value'];
                for(const ws of require('../websocket').wss.clients) {
                    if(ws.hasCapability(Capabilities.CAPSETTINGUPD)) {
                        ws.send(JSON.stringify({opcode: StringOpcodes.SETTINGS_UPDATE, name: setting.name, value: setting.value}));
                    }
                }
                websocket.respond(json, {ok: true});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SETTING_FIELD});
        }
    }
}