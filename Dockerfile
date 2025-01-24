#FROM openjdk:17.0.2

#WORKDIR /app

#COPY ./target/usuarios-0.0.1-SNAPSHOT.jar .

#EXPOSE 8001

#ENTRYPOINT ["java","-jar","usuarios-0.0.1-SNAPSHOT.jar"]

#se copia el directorio completo en el workdir para generar el .jar cuando se lance el build de la imagen
#se busca optimizar el build del contenedor

#WORKDIR /app

#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./

#RUN ./mvnw dependency:go-offline -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true

#COPY src ./src

#RUN ./mvnw clean package -DskipTests

#EXPOSE 8001

#ENTRYPOINT ["java","-jar","./target/usuarios-0.0.1-SNAPSHOT.jar"]

#FROM maven:3.8.3-openjdk-17

#RUN mvn -v

#####

#FROM openjdk:17-jdk-slim AS build

#COPY pom.xml mvnw ./
#COPY .mvn .mvn
#RUN ./mvnw dependency:resolve -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true

#COPY src src
#RUN ./mvnw package

#FROM openjdk:17-jdk-slim
#WORKDIR demo
#COPY --from=build target/*.jar demo.jar

FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn dependency:go-offline -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true
RUN mvn clean package -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true

FROM openjdk:17.0.2

WORKDIR /app

RUN mkdir ./logs

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8001

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar","app.jar"]
