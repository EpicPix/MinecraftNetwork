import path from 'path';
import fs from 'fs';
import { Router } from 'express';

export async function createRouter() {
    var router = Router();
    router.use(function(req, res, next) {
        res.setHeader('Cache-Control', 'no-store');
        next();
    });
    var endpoints = path.resolve(__dirname, 'endpoints');
    for(var endpoint of fs.readdirSync(endpoints)) {
        var endpointObj = await import(`./endpoints/${endpoint}`);
        var method = endpointObj.method.toLowerCase();
        if(!router[method]) {
            router.use(`${endpointObj.endpoint}`, endpointObj.handler);
        }else {
            router[method](`${endpointObj.endpoint}`, endpointObj.handler);
        }
    }
    return router;
}