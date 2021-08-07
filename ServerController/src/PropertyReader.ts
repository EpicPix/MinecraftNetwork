import fs from 'fs';

function readFile(file) {
    var contents = fs.readFileSync(file).toString();
    var lines = contents.split('\n');
    for(var i = 0; i<lines.length; i++) {
        if(!lines[i]) {
            lines.splice(i, 1);
            i--;
        }
    }
    var output = {};
    for(var i = 0; i<lines.length; i++) {
        var things = lines[i].split('=');
        var name = things[0];
        var data = things.slice(1).join('=');
        
        if(!isNaN(parseInt(data))) {
            output[name] = parseInt(data);
        }else if(data==='true' || data==='false') {
            output[name] = data==='true';
        }else {
            output[name] = data;
        }
    }
    return output;
}

module.exports = { readFile };