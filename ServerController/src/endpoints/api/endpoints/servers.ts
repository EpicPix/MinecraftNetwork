import { servers } from '../../../index';

module.exports = {
    method: 'GET',
    endpoint: '/servers',
    handler: function(req, res) {
        var pubServers = [];
        servers.forEach((server) => pubServers.push(server.public));
        res.sendJSON(pubServers);
    }
}