const fs = require('fs');

const { Server } = require('ws');
const { StringOpcodes, toOpcodeId, ErrorNumbers } = require('./opcodes');

const port = 8080;

var logins = [];
var servers = [];
var settings = [];
var ranks = [];
var players = [];
//00-ff will have it's own entry in players
for(var p = 0; p<16*16; p++) {
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
    var prefixindex = parseInt(uuid.slice(0, 2), 16);
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
    var prefixindex = parseInt(uuid.slice(0, 2), 16);
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

var t = false;

process.once('exit', exit);
process.once('SIGINT', exit);
process.once('SIGTERM', exit);

function exit() {
    if(!t) {
        save();
        console.log('\nSaved.\n');
    }
    t = true;
    process.exit(0);
}

if(!fs.existsSync('files')) {
    fs.mkdirSync('files');
}

const path = require('path');
const mainFolder = path.resolve(__dirname, '..');
const filesFolder = path.resolve(mainFolder, 'files');
const currentFolder = path.resolve(filesFolder, 'current');
const backupFolder = path.resolve(filesFolder, 'backup');
const backupPath = function(date) {
    return path.resolve(backupFolder, `backup-${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()} ${date.getHours().toString().padStart(2, '0')}.${date.getMinutes().toString().padStart(2, '0')}`);
}

const currentAuthFile = path.resolve(currentFolder, 'auth.json');
const currentSettingsFile = path.resolve(currentFolder, 'settings.json');
const currentRanksFile = path.resolve(currentFolder, 'ranks.json');
const currentPlayersFolder = path.resolve(currentFolder, 'players');

function makeSureExists(file, dir, content) {
    if(!content && content!=='') content = '[]';
    fs.mkdirSync(path.resolve(file, '..'), {recursive: true});
    if(!fs.existsSync(file)) {
        if(dir) {
            fs.mkdirSync(file);
        }else {
            fs.writeFileSync(file, content);
        }
        return true;
    }
    return false;
}

makeSureExists('secrets.properties', false, '');
var secrets = require('./PropertyReader').readFile('secrets.properties')

var webhook = secrets['webhook']; //Discord webhook, can be null

const https = require('https');

function sendWebhook(name, title, description, color) {
    if(webhook!=null) {
        var data = JSON.stringify({username: name, embeds: [{title, description, color}]});
        var req = https.request({
            hostname: "discord.com",
            port: 443,
            path: webhook.substring("https://discord.com".length, webhook.length),
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        }, (res) => {
            if(res.statusCode!==204) {
                console.log("An error occurred while sending webhook: " + res.statusCode + " " + res.statusMessage);
            }
        });
        
        req.write(data)
        req.end()
    }
}

function loadPlayers() {
    makeSureExists(currentPlayersFolder, true);
    for(var i = 0; i<256; i++) {
        var res = path.resolve(currentPlayersFolder, `${i.toString(16).padStart(2, '0')}.json`);
        if(fs.existsSync(res)) {
            players[i].push(...JSON.parse(fs.readFileSync(res)));
        }
    }
}

function load() {
    makeSureExists(currentAuthFile);
    makeSureExists(currentSettingsFile);
    makeSureExists(currentRanksFile);
    logins.push(...JSON.parse(fs.readFileSync(currentAuthFile)));
    settings.push(...JSON.parse(fs.readFileSync(currentSettingsFile)));
    ranks.push(...JSON.parse(fs.readFileSync(currentRanksFile)));
    loadPlayers();
}

function savePlayers() {
    makeSureExists(currentPlayersFolder, true);
    for(var i = 0; i<256; i++) {
        if(players[i].length!==0) {
            var res = path.resolve(currentPlayersFolder, `${i.toString(16).padStart(2, '0')}.json`);
            fs.writeFileSync(res, JSON.stringify(players[i]));
        }
    }
}

function save() {
    makeSureExists(currentAuthFile);
    makeSureExists(currentSettingsFile);
    makeSureExists(currentRanksFile);
    fs.writeFileSync(currentAuthFile, JSON.stringify(logins));
    fs.writeFileSync(currentSettingsFile, JSON.stringify(settings));
    fs.writeFileSync(currentRanksFile, JSON.stringify(ranks));
    savePlayers();
}
  
const targz = require('targz');

function backup() {
    makeSureExists(backupFolder, true);
    var date = new Date();
    console.log(`[${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}] Starting backup`);
    targz.compress({
        src: currentFolder,
        dest: `${backupPath(date)}.tar.gz`
    }, function(err){
        if(err) {
            console.log(err);
        } else {
            date = new Date();
            console.log(`[${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}] Finished backup`);
        }
    });
}

const wss = new Server({ noServer: true, path: "/wss" });
const express = require('express');
const app = express();
module.exports = { webhook, sendWebhook, logins, servers, settings, ranks, getPlayerByUUID, getPlayerByUsername, getPlayerOrCreate, getDefaultRank, updatePlayer, players, wss };

async function main() {

    load();
    getDefaultRank();

    if(logins.length===0) {
        console.log('No accounts are created, an account will be auto generated');
        console.log("Username and Password are 'admin'");
        logins.push({username: 'admin', password: 'admin'});
    }


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

    const server = app.listen(port);
    server.on('upgrade', (request, socket, head) => {
        wss.handleUpgrade(request, socket, head, (websocket) => {
            wss.emit("connection", websocket, request);
        });
    });
    console.log(`HTTP Server listening at port ${port}`)

    setInterval(() => {
        save();
    }, 1000*60*2);

    setInterval(() => {
        save();
        backup();
    }, 1000*60*60); //backup every 1 hour
}

Promise.resolve(main());