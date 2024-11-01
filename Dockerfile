FROM maven:3.9.9-amazoncorretto-21 AS build

COPY pom.xml .
COPY src ./src

ARG SPRING_PROFILE

RUN mvn package -P ${SPRING_PROFILE} -DskipTests

FROM amazoncorretto:21.0.4

ARG JAR_FILE=target/*.jar

COPY --from=build ${JAR_FILE} music.jar

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "music.jar"]