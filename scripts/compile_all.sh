#!/bin/bash

echo Checking libraries

sh scripts/get_libraries.sh

rm -rf builds

sh scripts/compile_bukkit.sh
sh scripts/compile_bungee.sh
sh scripts/compile_cli.sh
sh scripts/compile_modules.sh

echo Finished