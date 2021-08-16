#!/bin/bash

sh scripts/get_libraries.sh

echo Compiling Bukkit Plugin

mkdir -p compile/Bukkit/
cd compile/Bukkit/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/Bukkit/
cp -R Bukkit/src/* compile/Bukkit/
cd compile/Bukkit/
javac -cp ".:../../libraries/gson.jar:../../libraries/spigot.1.8.8.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf Bukkit.jar *

mkdir -p ../../builds/
cp Bukkit.jar ../../builds/Bukkit.jar
rm -r ../../compile