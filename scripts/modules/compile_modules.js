const libraries = ['bukkit', 'bungee']

const imports = {
    common: [
        "ga.epicpix.network.common.annotations.*",
        "ga.epicpix.network.common.modules.Module",
        "ga.epicpix.network.common.players.*",
        "ga.epicpix.network.common.ranks.*",
        "ga.epicpix.network.common.servers.*",
        "ga.epicpix.network.common.settings.*"
    ],
    bukkit: [
        "ga.epicpix.network.bukkit.Command",
        "ga.epicpix.network.bukkit.CommandContext"
    ],
    bungee: [
        "ga.epicpix.network.bungee.Command",
        "ga.epicpix.network.bungee.CommandContext"
    ]
}

const fs = require("fs");
const path = require("path");
const cp = require("child_process");
const { generate } = require("./module_gen");

const modulesPath = path.resolve(process.cwd(), "modules");
const modulesFiles = fs.readdirSync(modulesPath);

var error = false;

const modules = [];

for(const module of modulesFiles) {
    const modulePath = path.resolve(modulesPath, module);
    const moduleJsonPath = path.resolve(modulePath, 'module.json');
    if(!fs.existsSync(moduleJsonPath)) {
        // Using this to check for every module and check if it exists, then if there was an error.. exit
        console.error(`Missing module.json in module ${module}`);
        error = true;
        continue;
    }
    var json = JSON.parse(fs.readFileSync(moduleJsonPath).toString());
    modules.push([module, modulePath, json]);
}

if(error) {
    process.exit(1);
}

var moduleIds = [];

for(const [module,, moduleJson] of modules) {
    if(moduleIds.includes(moduleJson.id)) {
        console.error(`Duplicates of the same ID, ${moduleJson.id}`);
        error = true;
        continue;
    }
    moduleIds.push(moduleJson.id);
    if(!libraries.includes(moduleJson.library)) {
        console.error(`Unknown library '${moduleJson.library}' in module ${module}`); 
        error = true;
    }
}

if(error) {
    process.exit(1);
}

const cmd = function(run) {
    cp.execSync(run);
};

for(const [module, modulePath, moduleJson] of modules) {
    const id = moduleJson.id;
    fs.mkdirSync(path.resolve('compile', 'modules', id), {recursive: true});
    cp.execSync(`cp -r modules/${id}/ compile/modules/`);
    const files = [path.resolve('compile', 'modules', id)];
    for(var i = 0; i<files.length; i++) {
        var current = files[i];
        if(fs.lstatSync(current).isDirectory()) {
            var contents = fs.readdirSync(current);
            for(const f of contents) {
                files.push(path.resolve(current, f));
            }
        }else {
            const filename = path.basename(current);
            if(filename.endsWith('.m.java')) {
                const rel = current.replace(path.resolve('compile', 'modules').toString() + "/", '');
                const package = path.dirname(rel).replace('/', '.');
                var data = `package ${package};`;
                for(const imp of imports['common']) {
                    data += `import ${imp};`
                }
                for(const imp of imports[moduleJson.library]) {
                    data += `import ${imp};`
                }
                data += fs.readFileSync(current).toString();
                fs.rmSync(current);
                fs.writeFileSync(path.resolve('compile', 'modules', path.dirname(rel), filename.replace('.m.java', '.java')), data);
            }
        }
    }
    process.chdir(`compile/modules/${id}/`);
    var lib = "";
    if(moduleJson.library === 'bukkit') lib = '../../../../libraries/spigot.1.8.8.jar:../../../../builds/Bukkit.jar';
    else if(moduleJson.library === 'bungee') lib = '../../../../libraries/BungeeCord.jar:../../../../builds/BungeeCord.jar';
    fs.mkdirSync(id, {recursive: true});
    cmd(`mv *.java ${id}/`)
    process.chdir(id);
    cmd(`javac -cp ".:${lib}" $(find . -name "*.java")`);
    cmd('find . -name "*.java" -type f -delete');
    process.chdir('..');
    fs.writeFileSync('module.module', generate(moduleJson, id));
    cmd(`cp module.module ../../../builds/modules/${id}.module`);
    process.chdir('../../../')
}