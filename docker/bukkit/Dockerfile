FROM openjdk:11

EXPOSE 25565

RUN mkdir -p /server/
WORKDIR /server/

RUN wget -O spigot.jar https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar

COPY . .

ENTRYPOINT [ "java", "-Dcom.mojang.eula.agree=true", "-jar", "spigot.jar" ]