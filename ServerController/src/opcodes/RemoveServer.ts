import { ErrorNumbers } from '../opcodes'
import { servers } from '../index';
import { sendWebhook } from '../webhooks';

export function run(websocket, json) {
    if(websocket.checkAuth()) {
        if(json['server']) {
            for(var serv of servers) {
                if(serv.public.id === json['server']) {
                    const index = servers.indexOf(serv);
                    if (index > -1) {
                        servers.splice(index, 1);
                    }
                    break;
                }
            }
            sendWebhook('Server Manager', 'Server Removed', `The server \`${json['server']}\` got removed`, 0xE02222);
            websocket.respond(json, {ok: true});
        }else {
            websocket.respond(json, {ok: false, errno: ErrorNumbers.NO_SERVER_FIELD});
        }
    }
}