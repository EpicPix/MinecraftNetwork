import * as WebSocket from 'ws';
import { toOpcodeId } from './opcodes';
import { ErrorNumbers } from "./opcodes";

export const wss = new WebSocket.Server({ noServer: true, path: '/wss' });

interface UserData {

    authenticated: boolean,
    server: any,
    capabilities: number

}

export interface ClientWebSocket extends WebSocket {
    userData: UserData,
    respond: (request: any & {rid: number}, response: any & {rid: number}) => void,
    checkAuth: () => boolean,
    hasCapability: (capability: number) => boolean
}

wss.on('connection', function (ws: ClientWebSocket) {
    ws.userData = { authenticated: false, server: null, capabilities: 0 };
    ws.respond = function(message, data) {
        data.rid = message.rid;
        ws.send(JSON.stringify(data));
    };
    ws.checkAuth = function() {
        if(!ws.userData.authenticated) {
            ws.close(4006, "Not authenticated");
            return false;
        }
        return true;
    };
    ws.hasCapability = function(cap) {
        if((ws.userData.capabilities & cap) === cap) {
            return true;
        }
        return false;
    };
    ws.on('message', async function (message) {
        var json: object;
        try {
            json = JSON.parse(message.toString());
        } catch (error) {
            ws.close(4000, "Non-JSON data.");
        }
        
        var operationCode = json['opcode'];

        if(typeof operationCode === 'undefined' || operationCode === null) {
            ws.close(4002, 'Opcode not provided');
            return;
        }

        if(!Number.isInteger(operationCode)) {
            ws.close(4001, "Unknown opcode.");
            return;
        }

        var operationId = toOpcodeId(operationCode);

        if (operationId !== null) {
            try {
                (await import(`./opcodes/${operationId}`)).run(ws, json);
            }catch(error) {
                console.log(error);
                ws.respond(json, {ok: false, errno: ErrorNumbers.INTERNAL_ERROR});
            }
        } else {
            ws.close(4001, "Unknown opcode.");
        }
    });
});

export function bindWebsocketToServer(server) {
    server.on('upgrade', (request, socket, head) => {
        wss.handleUpgrade(request, socket, head, (websocket) => {
            wss.emit("connection", websocket, request);
        });
    });
}