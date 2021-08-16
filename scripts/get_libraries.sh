#!/bin/bash

mkdir -p libraries

if [ ! -f libraries/gson.jar ]; then
    wget -O libraries/gson.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar
fi

if [ ! -f libraries/spigot.1.8.8.jar ]; then
    wget -O libraries/spigot.1.8.8.jar https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar
fi

if [ ! -f libraries/BungeeCord.jar ]; then
    wget -O libraries/BungeeCord.jar https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar
fi
