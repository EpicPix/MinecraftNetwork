import https from 'https';
import { makeSureExists } from './saver';
import { readFile } from './PropertyReader';

makeSureExists('secrets.properties', false, '');
var secrets = readFile('secrets.properties')

var webhook = secrets['webhook']; //Discord webhook, can be null

export function sendWebhook(name: string, title: string, description: string, color: number) {
    if(webhook!==null && webhook!=='') {
        var data = JSON.stringify({username: name, embeds: [{title, description, color}]});
        var req = https.request({
            hostname: "discord.com",
            port: 443,
            path: webhook.substring("https://discord.com".length, webhook.length),
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        }, (res) => {
            if(res.statusCode!==204) {
                console.log("An error occurred while sending webhook: " + res.statusCode + " " + res.statusMessage);
            }
        });
        
        req.write(data)
        req.end()
    }
}