import { Capabilities, ErrorNumbers, StringOpcodes } from '../opcodes'
import { settings } from '../index';
import { wss, ClientWebSocket } from '../websocket';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['setting']) {
            if(json['default']) {
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
                    setting = {name: json['setting'], value: json['default']};
                    settings.push(setting);
                    for(const ws of wss.clients as Set<ClientWebSocket>) {
                        if(ws.hasCapability(Capabilities.CAPSETTINGUPD)) {
                            ws.send(JSON.stringify({opcode: StringOpcodes.SETTINGS_UPDATE, setting: setting}));
                        }
                    }
                    websocket.respond(json, {ok: true, setting: setting.value});
                }
            }else {
                websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_DEFAULT_FIELD});
            }
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SETTING_FIELD});
        }
    }
}