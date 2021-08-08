import { backup } from './backups';
import { startServer } from './server';

process.on('uncaughtException', function(exception) {
    console.log(exception);
});

export const logins = [];
export const servers = [];
export const settings = [];
export const ranks = [];
export const players = [];
//00-ff will have it's own entry in players
for(var p = 0; p<16*16; p++) {
    players.push([]);
}

import { load, save } from './saver';

export function getDefaultRank() {
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

export function updatePlayer(player, data) {
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

export function getPlayerOrCreate(player) {
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

export function getPlayerByUUID(uuid) {
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

export function getPlayerByUsername(username) {
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
load();
getDefaultRank();

if(logins.length===0) {
    console.log('No accounts are created, an account will be auto generated');
    console.log("Username and Password are 'admin'");
    logins.push({username: 'admin', password: 'admin'});
}

startServer(8080).then(() => {
    backup();

    setInterval(() => {
        save();
    }, 1000*60*2);

    setInterval(() => {
        save();
        backup();
    }, 1000*60*30); //backup every 30 minutes
});