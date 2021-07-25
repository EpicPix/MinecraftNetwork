const fs = require('fs');

const { Server } = require('ws');
const { StringOpcodes, toOpcodeId } = require('./opcodes');

const port = 8080;
const wss = new Server({ port });

var logins = [];
var servers = [];
var settings = [];
var ranks = [];
var players = [];
//0000-ffff will have it's own entry in players
for(var p = 0; p<16*16*16*16; p++) {
    players.push([]);
}

function getDefaultRank() {
    if(ranks.length==0) {
        ranks.push({id: "default", priority: 0, prefix: [], suffix: [], permissions: [], nameColor: "white", chatColor: "white"});
    }
    var lowestRank = null;
    for(var rank of ranks) {
        if(!lowestRank || rank.priority<lowestRank.priority) {
            lowestRank = rank;
        }
    }
    return lowestRank;
}

function updatePlayer(player, data) {
    var copy = function(a, b, c) {
        if(typeof b[c] !== 'undefined') {
            a[c] = b[c];
        }
    }
    copy(player, data, 'username');
    copy(player, data, 'rank');
    copy(player, data, 'firstLogin');
    copy(player, data, 'lastLogin');
}

function getPlayerOrCreate(player) {
    var uuid = player.uuid;
    var prefixindex = parseInt(uuid.slice(0, 4), 16);
    var pplayers = players[prefixindex];
    var p1 = getPlayerByUUID(uuid);
    if(p1) {
        return p1;
    }
    var p2 = getPlayerByUsername(player.username);
    if(p2) {
        return p2;
    }
    
    pplayers.push(player);
    return player;
}

function getPlayerByUUID(uuid) {
    var prefixindex = parseInt(uuid.slice(0, 4), 16);
    var pplayers = players[prefixindex];
    if(pplayers.length==0) {
        return null;
    }
    //probably also inefficient
    for(var player of pplayers) {
        if(player.uuid==uuid) {
            return player;
        }
    }
    return null;
}

//more inefficient because it has to go thru each player, this will probably have indexing
function getPlayerByUsername(username) {
    for(var pplayers of players) {
        for(var player of pplayers) {
            if(player.username===username) {
                return player;
            }
        }
    }
    return null;
}

module.exports = { logins, servers, settings, ranks, getPlayerByUUID, getPlayerByUsername, getPlayerOrCreate, getDefaultRank, updatePlayer, players, wss };

async function main() {

    console.log('No accounts are created, login using admin account');
    console.log("Username and Password are 'admin'")
    logins.push({username: 'admin', password: 'admin'})
    


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
                require(`./opcodes/${operationId}`)(ws, json);
            } else {
                ws.close(4001, "Unknown opcode.");
            }
        });
    });

    console.log(`WebSocket Server listening at port ${port}`)
}

Promise.resolve(main());