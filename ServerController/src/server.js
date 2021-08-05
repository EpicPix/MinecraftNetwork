const express = require('express');
const fs = require('fs');
const path = require('path');

function startServer(port) {
    const app = express();
    var endpoints = path.resolve(__dirname, 'endpoints');
    for(var endpoint of fs.readdirSync(endpoints)) {
        var filepath = path.resolve(endpoints, endpoint);
        if(fs.lstatSync(filepath).isDirectory()) {
            var index = path.resolve(filepath, 'index.js');
            if(!fs.existsSync(index)) {
                console.log(`Endpoint ${endpoint} does not have index.js!`);
            }else {
                var endpointObj = require(`./endpoints/${endpoint}/index`);
                if(!endpointObj.createRouter) {
                    console.log(`Endpoint ${endpoint} does not have createRouter function!`);
                }else {
                    app.use(`/${endpoint}`, endpointObj.createRouter(express));
                    console.log(`Endpoint /${endpoint} created`);
                }
            }
        }else {
            console.log(`Endpoint ${endpoint} is not a directory!`);
        }
    }
    const server = app.listen(port);
    require('./websocket').bindWebsocketToServer(server);
    console.log(`HTTP Server listening at port ${port}`);
}

module.exports = { startServer }