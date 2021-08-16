FROM openjdk:11

EXPOSE 25577

RUN mkdir -p /server/
WORKDIR /server/

RUN wget -O BungeeCord.jar https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar

COPY . .

ENTRYPOINT [ "java", "-jar", "BungeeCord.jar" ]