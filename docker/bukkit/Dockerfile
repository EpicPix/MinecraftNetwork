FROM openjdk:11

EXPOSE 25565

RUN mkdir -p /run/
WORKDIR /run/

RUN mkdir -p plugins/
COPY ../../builds/Bukkit.jar plugins/

RUN wget -O spigot.jar https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar

COPY . .

CMD [ "java", "-Dcom.mojang.eula.agree=true", "-jar", "spigot.jar" ]