#!/bin/bash

mkdir compile
if [ ! -d libraries ]; then
    mkdir libraries
fi

if [ ! -f libraries/gson.jar ]; then
    wget -O libraries/gson.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar
fi

if [ ! -f libraries/spigot.1.8.8.jar ]; then
    wget -O libraries/spigot.1.8.8.jar https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar
fi

if [ ! -f libraries/BungeeCord.jar ]; then
    wget -O libraries/BungeeCord.jar https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar
fi

mkdir compile/Bukkit/
cd compile/Bukkit/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/Bukkit/
cp -R Bukkit/src/* compile/Bukkit/
cd compile/Bukkit/
javac -cp ".:../../libraries/gson.jar:../../libraries/spigot.1.8.8.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf Bukkit.jar *

cd ../../

mkdir compile/BungeeCord/
cd compile/BungeeCord/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/BungeeCord/
cp -R BungeeCord/src/* compile/BungeeCord/
cd compile/BungeeCord/
javac -cp ".:../../libraries/gson.jar:../../libraries/BungeeCord.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf BungeeCord.jar *

cd ../../

mkdir compile/CLI/
cd compile/CLI/
jar xf ../../libraries/gson.jar
cd ../../
cp -R Common/src/* compile/CLI/
cp -R CLI/src/* compile/CLI/
cd compile/CLI/
javac -cp ".:../../libraries/gson.jar" $(find . -name "*.java")
find . -name "*.java" -type f -delete
jar cf CLI.jar *

cd ../../

rm -r builds
mkdir builds
cp compile/Bukkit/Bukkit.jar builds/Bukkit.jar
cp compile/BungeeCord/BungeeCord.jar builds/BungeeCord.jar
cp compile/CLI/CLI.jar builds/CLI.jar

rm -r compile