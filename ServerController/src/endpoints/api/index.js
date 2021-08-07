const path = require('path');
const fs = require('fs');

function createRouter(express) {
    var router = express.Router();
    router.use(function(req, res, next) {
        res.setHeader('Cache-Control', 'no-store');
        next();
    });
    var endpoints = path.resolve(__dirname, 'endpoints');
    for(var endpoint of fs.readdirSync(endpoints)) {
        var endpointObj = require(`./endpoints/${endpoint}`);
        var method = endpointObj.method.toLowerCase();
        if(!router[method]) {
            router.use(`${endpointObj.endpoint}`, endpointObj.handler);
        }else {
            router[method](`${endpointObj.endpoint}`, endpointObj.handler);
        }
    }
    return router;
}

module.exports = { createRouter }