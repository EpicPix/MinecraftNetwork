#!/bin/bash

echo Compiling modules

rm -rf builds/modules/
mkdir -p builds/modules/

node scripts/modules/compile_modules.js

rm -rf compile