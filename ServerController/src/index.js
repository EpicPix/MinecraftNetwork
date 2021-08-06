Array.prototype.removeElement = function(value) { 
    return this.filter(function(ele){ 
        return ele != value; 
    });
}

process.on('uncaughtException', function(exception) {
    console.log(exception);
});

var logins = [];
var servers = [];
var settings = [];
var ranks = [];
var players = [];
//00-ff will have it's own entry in players
for(var p = 0; p<16*16; p++) {
    players.push([]);
}

module.exports = { logins, servers, settings, ranks, players };

const {load, save, makeSureExists} = require('./saver');

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

module.exports = { logins, servers, settings, ranks, getPlayerByUUID, getPlayerByUsername, getPlayerOrCreate, getDefaultRank, updatePlayer, players };

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

async function main() {

    load();
    getDefaultRank();

    if(logins.length===0) {
        console.log('No accounts are created, an account will be auto generated');
        console.log("Username and Password are 'admin'");
        logins.push({username: 'admin', password: 'admin'});
    }

    require('./server').startServer(8080);
    require('./backups').backup();

    setInterval(() => {
        save();
    }, 1000*60*2);

    setInterval(() => {
        save();
        require('./backups').backup();
    }, 1000*60*30); //backup every 30 minutes
}

Promise.resolve(main());