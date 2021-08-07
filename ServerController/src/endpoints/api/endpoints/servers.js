module.exports = {
    method: 'GET',
    endpoint: '/servers',
    handler: function(req, res) {
        var servers = [];
        require('../../../index').servers.forEach((server) => servers.push(server.public));
        res.sendJSON(servers);
    }
}