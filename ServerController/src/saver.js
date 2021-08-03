const fs = require('fs');
var { logins, settings, ranks, players } = require('./index');

if(!fs.existsSync('files')) {
    fs.mkdirSync('files');
}

const path = require('path');

function makeSureExists(file, dir, content) {
    if(!content && content!=='') content = '[]';
    fs.mkdirSync(path.resolve(file, '..'), {recursive: true});
    if(!fs.existsSync(file)) {
        if(dir) {
            fs.mkdirSync(file);
        }else {
            fs.writeFileSync(file, content);
        }
        return true;
    }
    return false;
}

const mainFolder = path.resolve(__dirname, '..');
const filesFolder = path.resolve(mainFolder, 'files');
const currentFolder = path.resolve(filesFolder, 'current');

const currentAuthFile = path.resolve(currentFolder, 'auth.json');
const currentSettingsFile = path.resolve(currentFolder, 'settings.json');
const currentRanksFile = path.resolve(currentFolder, 'ranks.json');
const currentPlayersFolder = path.resolve(currentFolder, 'players');

function loadPlayers() {
    makeSureExists(currentPlayersFolder, true);
    for(var i = 0; i<256; i++) {
        var res = path.resolve(currentPlayersFolder, `${i.toString(16).padStart(2, '0')}.json`);
        if(fs.existsSync(res)) {
            players[i].push(...JSON.parse(fs.readFileSync(res)));
        }
    }
}

function load() {
    makeSureExists(currentAuthFile);
    makeSureExists(currentSettingsFile);
    makeSureExists(currentRanksFile);
    logins.push(...JSON.parse(fs.readFileSync(currentAuthFile)));
    settings.push(...JSON.parse(fs.readFileSync(currentSettingsFile)));
    ranks.push(...JSON.parse(fs.readFileSync(currentRanksFile)));
    loadPlayers();
}

function savePlayers() {
    makeSureExists(currentPlayersFolder, true);
    for(var i = 0; i<256; i++) {
        if(players[i].length!==0) {
            var res = path.resolve(currentPlayersFolder, `${i.toString(16).padStart(2, '0')}.json`);
            fs.writeFileSync(res, JSON.stringify(players[i]));
        }
    }
}

function save() {
    makeSureExists(currentAuthFile);
    makeSureExists(currentSettingsFile);
    makeSureExists(currentRanksFile);
    fs.writeFileSync(currentAuthFile, JSON.stringify(logins));
    fs.writeFileSync(currentSettingsFile, JSON.stringify(settings));
    fs.writeFileSync(currentRanksFile, JSON.stringify(ranks));
    savePlayers();
}

module.exports = {save, load, filesFolder, currentFolder, makeSureExists}