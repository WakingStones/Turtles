FROM openjdk:8-jdk as builder

WORKDIR /buildpath

COPY . .

ARG callersbane_url="https://download.scrolls.com/callersbane/server/CallersBane-Server-2.0.1.zip"

RUN /bin/bash -c 'if [[ ! -f "callersbane.zip" ]]; then wget -O "callersbane.zip" "${callersbane_url}"; fi;'

RUN ./gradlew prepareWorkspace && \
    ./gradlew build

FROM openjdk:8

WORKDIR /scrolls

COPY --from=builder /buildpath/build/libs ./

COPY *.json ./
COPY entrypoint.sh .

ARG memory=4G

ENV CALLERSBANE_URL=$callersbane_url
ENV MEMORY=$memory
ENV NODE_ID=test-server

ENV DB_HOST="database"
ENV DB_USER=scrolls
ENV DB_PASS=scrolls

EXPOSE 8081
EXPOSE 8082

ENTRYPOINT ["./entrypoint.sh"]