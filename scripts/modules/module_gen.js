const fs = require('fs');
const path = require("path");

function generate(module, folder) {

    const classes = [];
    const travel = [folder];
    for(let i = 0; i<travel.length; i++) {
        let current = travel[i];
        if(fs.lstatSync(current).isDirectory()) {
            var contents = fs.readdirSync(current);
            for(const f of contents) {
                travel.push(current + "/" + f);
            }
        }else {
            const filename = path.basename(current);
            if(filename.endsWith('.class')) {
                classes.push(current);
            }
        }
    }

    const smodule = JSON.stringify(module);
    
    const bmlen = Buffer.alloc(2);
    bmlen.writeUInt16BE(smodule.length);

    const files = Buffer.alloc(2);
    files.writeUInt16BE(classes.length);

    const buffers = [];

    for(let i = 0; i<classes.length; i++) {
        const len = Buffer.alloc(2);
        len.writeUInt16BE(classes[i].length);
        buffers.push(len);
        buffers.push(Buffer.from(classes[i]));
        const d = fs.readFileSync(classes[i]);
        const dlen = Buffer.alloc(4);
        dlen.writeUInt32BE(d.length);
        buffers.push(dlen);
        buffers.push(d);
    }

    return Buffer.concat([bmlen, Buffer.from(smodule), files, ...buffers]);
}

module.exports = { generate }