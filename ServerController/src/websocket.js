const { Server } = require('ws');
const { ErrorNumbers, toOpcodeId } = require('./opcodes');

const wss = new Server({ noServer: true, path: '/wss' });

wss.on('connection', function (ws) {
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
    ws.on('message', function (message) {
        var json;
        try {
            json = JSON.parse(message);
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
                require(`./opcodes/${operationId}`)(ws, json);
            }catch(error) {
                console.log(error);
                ws.respond(json, {ok: false, errno: ErrorNumbers.INTERNAL_ERROR});
            }
        } else {
            ws.close(4001, "Unknown opcode.");
        }
    });
});

function bindWebsocketToServer(server) {
    server.on('upgrade', (request, socket, head) => {
        wss.handleUpgrade(request, socket, head, (websocket) => {
            wss.emit("connection", websocket, request);
        });
    });
}

module.exports = { bindWebsocketToServer, wss };