import { versionList } from '../../../version';

module.exports = {
    method: 'GET',
    endpoint: '/versions',
    handler: function(req, res) {
        res.sendJSON(versionList);
    }
}