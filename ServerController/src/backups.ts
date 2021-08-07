import { makeSureExists, filesFolder, currentFolder } from './saver'

import tar from 'tar';
import path from 'path';
import fs from 'fs';

const backupFolder = path.resolve(filesFolder, 'backup');
const backupPath = function(date) {
    return path.resolve(backupFolder, `backup-${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()} ${date.getHours().toString().padStart(2, '0')}.${date.getMinutes().toString().padStart(2, '0')}`);
}

function backup() {
    makeSureExists(backupFolder, true);
    var date = new Date();
    console.log(`[${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}] Starting backup`);
    tar.c(
        {
            gzip: true,
            file: `${backupPath(date)}.tar.gz`,
            cwd: currentFolder
        },
        fs.readdirSync(currentFolder)
    ).then(_ => {
        date = new Date();
        console.log(`[${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}] Finished backup`);
    });
}

module.exports = { backup }