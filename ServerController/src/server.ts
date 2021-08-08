import express from 'express';
import fs from 'fs';
import path from 'path';

import { bindWebsocketToServer } from './websocket';

interface ClientResponse extends express.Response {
    sendJSON: (json: string | object, code: number) => void;
}

export async function startServer(port: number) {
    const app = express();
    var endpoints = path.resolve(__dirname, 'endpoints');
    app.disable('x-powered-by');
    app.use(function(req, res: ClientResponse, next) {
        res.sendJSON = function(json: string | object, code: undefined | number) {
            if(typeof json !== 'string') {
                json = JSON.stringify(json);
            }
            if(!code) {
                code = 200;
            }
            res.contentType('application/json');
            res.status(code);
            res.send(json);
        };
        next();
    });
    for(var endpoint of fs.readdirSync(endpoints)) {
        var filepath = path.resolve(endpoints, endpoint);
        if(fs.lstatSync(filepath).isDirectory()) {
            var index = path.resolve(filepath, 'index.js');
            if(!fs.existsSync(index)) {
                console.log(`Endpoint ${endpoint} does not have index.js!`);
            }else {
                var endpointObj = await import(`./endpoints/${endpoint}/index`);
                if(!endpointObj.createRouter) {
                    console.log(`Endpoint ${endpoint} does not have createRouter function!`);
                }else {
                    app.use(`/${endpoint}`, await endpointObj.createRouter());
                    console.log(`Endpoint /${endpoint} created`);
                }
            }
        }else {
            console.log(`Endpoint ${endpoint} is not a directory!`);
        }
    }
    const server = app.listen(port);
    bindWebsocketToServer(server);
    console.log(`HTTP Server listening at port ${port}`);
}