module.exports = function(websocket, json) {
    if(websocket.checkAuth()) {
        const ranks = require('../index').ranks;
        if(ranks.length==0) {
            ranks.push({id: "default", priority: 0, prefix: [], suffix: [], permissions: [], nameColor: "white", chatColor: "white"});
        }
        var lowestRank = null;
        for(var rank of ranks) {
            if(!lowestRank || rank.priority<lowestRank.priority) {
                lowestRank = rank;
            }
        }
        websocket.respond(json, {ok: true, rank: lowestRank});
    }
}