#!/bin/bash

sh scripts/get_libraries.sh

echo Compiling CLI

mkdir -p compile/CLI/
cd compile/CLI/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/CLI/
cp -R CLI/src/* compile/CLI/
cd compile/CLI/
javac -cp ".:../../libraries/gson.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf CLI.jar *

mkdir -p ../../builds/
cp CLI.jar ../../builds/CLI.jar
rm -r ../../compile