const libraries = ['bukkit', 'bungee']

const fs = require("fs");
const path = require("path");

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

const cmd = console.log;

for(const [module, modulePath, moduleJson] of modules) {
    const id = moduleJson.id;
    cmd(`mkdir -p compile/modules/${id}/`);
    cmd(`cp -r modules/${id}/ compile/modules/${id}/`);
    cmd(`cd compile/modules/${id}/`);
    var lib = "";
    if(moduleJson.library === 'bukkit') lib = '../../../libraries/spigot.1.8.8.jar:../../../builds/Bukkit.jar';
    else if(moduleJson.library === 'bungee') lib = '../../../libraries/BungeeCord.jar:../../../builds/BungeeCord.jar';
    cmd(`javac -cp ".:${lib}" $(find . -name "*.java")`);
    cmd('find . -name "*.java" -type f -delete');
    cmd('jar cf module.jar *');
    cmd(`cp module.jar ../../../builds/modules/${id}.jar`);
    cmd(`cd ../../../`);
}