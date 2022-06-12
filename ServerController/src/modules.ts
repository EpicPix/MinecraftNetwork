import fs from 'fs';

export function getModuleInfo(moduleBytes: Buffer) {
    let jsonLen = moduleBytes.readInt16BE(0);
    let jsonText = moduleBytes.toString("utf8", 2, 2 + jsonLen);
    try {
        return JSON.parse(jsonText);
    }catch(e) {
        return null;
    }
}

export function saveModule(module: Buffer, id: string, version: string) {
    const filesPath = `./files/current/modules`;
    fs.mkdirSync(filesPath, {recursive: true});
    let fn = `${id}-${version}`;
    let alreadyExists = fs.existsSync(`${filesPath}/${fn}.module`);
    if(alreadyExists) {
        let lastVersionChange = 1;
        while(fs.existsSync(`${filesPath}/${fn}_${lastVersionChange}.module`)) {
            lastVersionChange++;
        }
        fs.renameSync(`${filesPath}/${fn}.module`, `${filesPath}/${fn}_${lastVersionChange}.module`)
    }
    fs.writeFileSync(`${filesPath}/${fn}.module`, module);
}

export function getModules() {
    const filesPath = `./files/current/modules`;
    fs.mkdirSync(filesPath, {recursive: true});
    const modules = [];
    for(let module of fs.readdirSync(filesPath)) {
        if(/^[a-zA-Z0-9_-]{2,}-v?[0-9]+(\.[0-9]+)*\.module$/.test(module)) {
            let bytes = fs.readFileSync(`${filesPath}/${module}`)
            modules.push([module, bytes, getModuleInfo(bytes)]);
        }
    }
    return modules;
}

export function getModule(id: string, version: string) {
    const filesPath = `./files/current/modules`;
    fs.mkdirSync(filesPath, {recursive: true});
    let module = `${id}-${version}.module`;
    if(!fs.existsSync(`${filesPath}/${module}`)) {
        return null;
    }
    let bytes = fs.readFileSync(`${filesPath}/${module}`)
    return [bytes, getModuleInfo(bytes)];
}