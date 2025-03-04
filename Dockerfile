FROM amazoncorretto:21

WORKDIR /app

COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN ls -l mvnw

RUN chmod +x mvnw

RUN yum install -y tar gzip

RUN ./mvnw clean package -DskipTests

RUN ls -l target/finances-ms-user-0.0.1-SNAPSHOT.jar

COPY target/finances-ms-user-0.0.1-SNAPSHOT.jar app.jar

RUN ls -l /app/app.jar

RUN bash -c 'touch /app/app.jar'

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]