FROM openjdk:11

EXPOSE 25577

RUN mkdir -p /run/
WORKDIR /run/

RUN mkdir -p plugins/
COPY ../../builds/BungeeCord.jar plugins/

RUN wget -O BungeeCord.jar https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar

COPY . .

CMD [ "java", "-jar", "BungeeCord.jar" ]