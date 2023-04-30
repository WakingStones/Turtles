#!/usr/bin/env bash

if [[ ! -f "Server.jar" ]]
then
  if [[ "${CALLERSBANE_URL}" = "https://*" ]]
  then
    curl -o callersbane.zip "${CALLERSBANE_URL}"
  else
    cp "${CALLERSBANE_URL}" callersbane.zip
  fi

  unzip callersbane.zip

  cp -rf CallersBane-Server/* .
  rm -rf CallersBane-Server
fi

echo "[client]
user=${DB_USER}
password=${DB_PASS}" > ~/.my.cnf

chmod 600 ~/.my.cnf

# update db with our IP in the config
mysql -c "UPDATE servers SET ip = '${SERVER_IP}', port = '${SERVER_PORT}' WHERE id = ''"

java \
    -Xmx${MEMORY} \
    -Dio.netty.epollBugWorkaround=true \
    -Dscrolls.mode=PROD \
    -Dscrolls.node.id=${NODE_ID} \
    -Dhibernate.connection.url="${DB_URL}" \
    -Dhibernate.connection.username="${DB_USER}" \
    -Dhibernate.connection.password="${DB_PASS}" \
    -jar turtles-3.0-SNAPSHOT.jar