FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY . /app
RUN javac -cp "classes:depedencies/*:." -d classes \
    src/com/client/server/protocol/*.java \
    src/com/chat/dl/exceptions/*.java \
    src/com/chat/dl/interfaces/dto/*.java \
    src/com/chat/dl/interfaces/dao/*.java \
    src/com/chat/dl/dto/*.java \
    src/com/chat/dl/dao/*.java \
    src/com/chat/server/*.java
CMD java -classpath "classes:depedencies/*" com.chat.server.DrossChatServer
