#!/bin/bash

echo Compiling modules

rm -rf builds/modules/
mkdir -p builds/modules/

eval "$(node scripts/compile_modules.js)"

rm -rf compile