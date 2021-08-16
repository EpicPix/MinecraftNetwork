#!/bin/bash

sh scripts/get_libraries.sh

echo Compiling Bungee Plugin

mkdir -p compile/BungeeCord/
cd compile/BungeeCord/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/BungeeCord/
cp -R BungeeCord/src/* compile/BungeeCord/
cd compile/BungeeCord/
javac -cp ".:../../libraries/gson.jar:../../libraries/BungeeCord.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf BungeeCord.jar *

mkdir -p ../../builds/
cp BungeeCord.jar ../../builds/BungeeCord.jar
rm -r ../../compile