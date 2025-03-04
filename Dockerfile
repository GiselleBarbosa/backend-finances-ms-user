FROM amazoncorretto:21

WORKDIR /app

COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN ls -l mvnw

RUN chmod +x mvnw

RUN yum install -y tar gzip

ENV BUILD_DATE=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
RUN echo "Build date: $BUILD_DATE"
RUN printenv BUILD_DATE

# Adicionando esta linha para forçar a reconstrução
RUN touch src/main/java/com/example/financesmsuser/FinancesMsUserApplication.java

RUN java -version
RUN ./mvnw -version

RUN ./mvnw clean package -DskipTests

RUN ls -l target/
RUN ls -l

COPY target/finances-ms-user-0.0.1-SNAPSHOT.jar app.jar

RUN ls -l

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]