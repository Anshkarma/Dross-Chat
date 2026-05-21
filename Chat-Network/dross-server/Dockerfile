FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY . /app
# Run the compilation inside the cloud container (Linux-compliant classpath spacing)
RUN javac -cp "classes:depedencies/*:." -d classes src/com/chat/server/*.java
CMD ["java", "-classpath", "classes:depedencies/*", "com.chat.server.DrossChatServer"]
