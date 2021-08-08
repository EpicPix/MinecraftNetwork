import { ErrorNumbers } from '../opcodes'
import { settings } from '../index';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['setting']) {
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