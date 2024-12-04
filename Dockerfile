FROM openjdk:21

EXPOSE 8080

WORKDIR /app

COPY /target/chatGPT-*.jar  /app/telegram-app.jar

CMD ["java", "-jar", "/app/telegram-app.jar"]