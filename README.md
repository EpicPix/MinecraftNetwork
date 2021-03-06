# MinecraftNetwork

[![Compile & Deploy](https://github.com/EpicPix/MinecraftNetwork/actions/workflows/main.yml/badge.svg)](https://github.com/EpicPix/MinecraftNetwork/actions/workflows/main.yml)

A open source Minecraft Network Project using BungeeCord and Spigot with Node.js to create a ServerManager (Alpha)

While compiling you will need to change [secrets.properties](Common/src/secrets.properties) to contain the secrets of the ServerManager

A ServerManager is a Server which will control all settings, player data, servers. Without it the project won't run.

# Requirements

You will require

 - Java 11 Java Development Kit (For compiling)
 - A server to host the server on
 - Node.js hosting

*The hosting can be localhost for testing and another purposes, but i would recommend putting it on a host for other purposes like hosting a actual server*

More data will be added later

# Dependencies

## Java

 - Spigot (Bukkit Module)
 - BungeeCord (BungeeCord Module)
 - Gson (Common Module)

 The common module is used in CLI, BungeeCord and Bukkit modules

 ## Node

 To install dependencies for node you have to go to the ServerController folder and run `npm install` and that will install all dependencies for node

# Building

## Java

Go to the root directory of the project and run
`sh scripts/compile_all.sh` to compile everything like

- Bukkit Plugin
- BungeeCord Plugin
- CLI
- Modules

and you can also run an invidiual script like `scripts/compile_modules.sh` but for that one you will need to already have Bukkit plugin and BungeeCord plugin installed, you will find the built jar in `builds/` and for modules its `builds/modules/`

## Node

You just have to run `npx tsc` in the `ServerController` folder

# Starting

## Bukkit/BungeeCord

Todo.

## ServerController

To start ServerController go to the `ServerController` folder and run `npm start` which will compile and start the ServerController