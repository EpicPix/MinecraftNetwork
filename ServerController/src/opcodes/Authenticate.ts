module.exports = function(websocket, json) {
    if(!websocket.userData.authenticated) {
        var username = json['username'];
        var password = json['password'];
        if(typeof username === undefined || username === null) {
            websocket.close(4003, 'Username not provided');
            return;
        }
        if(typeof password === undefined || password === null) {
            websocket.close(4004, 'Password not provided');
            return;
        }
        const logins = require('../index').logins;
        var success = logins.length===0;
        for(const login of logins) {
            if(login['username']===username && login['password']===password) {
                success = true;
                break;
            }
        }
        websocket.userData.authenticated = success;
        websocket.userData.clientType = json['clientType'];
        websocket.userData.capabilities = json['capabilities'];
        websocket.respond(json, {success});
        if(!success) websocket.close(4005, 'Authentication failed');
    }
}