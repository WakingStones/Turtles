FROM openjdk:8 as builder

RUN apt update && apt install -y --no-install-recommends dos2unix

WORKDIR /buildpath

COPY . .

RUN /bin/bash -c 'if [[ ! -f "callersbane.zip" ]]; then wget -O "callersbane.zip" "${callersbane_url}"; fi;'

RUN dos2unix gradlew && \
    dos2unix entrypoint.sh && \
    chmod +x gradlew entrypoint.sh && \
    ./gradlew prepareWorkspace && \
    ./gradlew build

FROM openjdk:8

RUN apt update && \
    apt install -y mariadb-client && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /scrolls

COPY --from=builder /buildpath/build/libs ./
COPY --from=builder /buildpath/entrypoint.sh ./

COPY *.json ./

ARG memory=4G

ENV MEMORY=$memory
ENV NODE_ID=test-server

ENV DB_HOST="database"
ENV DB_USER=scrolls
ENV DB_PASS=scrolls

EXPOSE 8081
EXPOSE 8082

ENTRYPOINT ["./entrypoint.sh"]