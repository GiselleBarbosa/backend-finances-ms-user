FROM amazoncorretto:21

WORKDIR /app

COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN ls -l mvnw
RUN ls -l .

RUN chmod +x mvnw

RUN yum install -y tar gzip

RUN ./mvnw clean package -DskipTests

RUN ls -l target/
RUN ls -l

COPY target/finances-ms-user-0.0.1-SNAPSHOT.jar app.jar

RUN ls -l

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]