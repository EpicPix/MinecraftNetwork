const fs = require('fs');

const { Server } = require('ws');
const { MongoClient } = require('mongodb');
const { Secrets } = require('./secrets');
const { StringOpcodes, OpcodeHandler, toOpcodeFunctionName } = require('./opcodes');

if (!fs.existsSync("secrets.json")) {
    fs.writeFileSync("secrets.json", JSON.stringify({}));
}

function getSecrets() {
    const secretsstr = fs.readFileSync("secrets.json", 'utf8');
    var secretsjson;
    try {
        secretsjson = JSON.parse(secretsstr);
    } catch (error) {
        console.error("An error occurred while parsing secrets.json");
        process.exit();
    }
    return new Secrets(secretsjson.mongodb);
}

var logins = [];

module.exports = { logins };

const port = 8080;

async function main() {

    const Mongo = new MongoClient(getSecrets().getConnectionString());
    await Mongo.connect();

    console.log('Connected to MongoDB')

    logins.push(...await Mongo.db('data').collection('auth').find().toArray());

    if(logins.length === 0) {
        console.log('No accounts are created, login using admin account');
        console.log("Username and Password are 'admin'")
        logins.push({username: 'admin', password: 'admin'})
    }

    const wss = new Server({ port });

    wss.on('connection', function (ws) {
        ws.userData = { authenticated: false };
        ws.respond = function(message, data) {
            data.rid = message.rid;
            ws.send(JSON.stringify(data));
        };
        ws.on('message', function (message) {
            var json;
            try {
                json = JSON.parse(message);
            } catch (error) {
                ws.close(4000, "Non-JSON data.");
            }
            
            var operationCode = json['opcode'];

            if(typeof operationCode === 'undefined' || operationCode === null) {
                ws.close(4002, 'Opcode not provided');
                return;
            }

            if(!Number.isInteger(operationCode)) {
                ws.close(4001, "Unknown opcode.");
                return;
            }

            var operationFunction = toOpcodeFunctionName(operationCode);

            if (operationFunction !== null) {
                OpcodeHandler[operationFunction](ws, json);
            } else {
                ws.close(4001, "Unknown opcode.");
            }
        });
    });

    console.log(`WebSocket Server listening at port ${port}`)

    /*

    var watchStream = Mongo.db('data').watch();

    while(true) {
        console.log(await watchStream.next());
    }

    */
}

Promise.resolve(main());