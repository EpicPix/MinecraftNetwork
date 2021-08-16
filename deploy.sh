#!/bin/bash

# Write code here deploy builds, locations of files:
#
# ./builds/Bukkit.jar  - Spigot Plugin
# ./builds/Bungee.jar  - BungeeCord Plugin
# ./builds/CLI.jar     - CLI

echo Copying plugins

mkdir -p docker/bukkit/plugins
cp ./builds/Bukkit.jar ./docker/bukkit/plugins/Plugin.jar

mkdir -p docker/bungee/plugins
cp ./builds/BungeeCord.jar ./docker/bungee/plugins/Plugin.jar

cd docker

mkdir -p images

cd bukkit
echo Building bukkit docker
docker build --no-cache -t net:Bukkit .

cd ../bungee

echo Building bungee docker
docker build --no-cache -t net:Bungee .