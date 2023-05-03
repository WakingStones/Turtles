#!/usr/bin/env bash

echo "[client]
user=${DB_USER}
password=${DB_PASS}
host=${DB_HOST}" > ~/.my.cnf

chmod 600 ~/.my.cnf

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

while ! mysqladmin ping --silent
do
    echo "Waiting for database to be active"
    sleep 1
done

result=$(mysql -s -N -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='scrolls'")
if [ -z "$result" ]
then
  echo "Creating database"
  mysql < callersbane_database.sql
fi

# update db with our IP in the config
mysql -e "UPDATE scrolls.servers SET ip = '${SERVER_IP}', port = '${SERVER_PORT}' WHERE id = '${NODE_ID}'"

java \
    -Xmx${MEMORY} \
    -Dio.netty.epollBugWorkaround=true \
    -Dscrolls.mode=PROD \
    -Dscrolls.node.id=${NODE_ID} \
    -Dhibernate.connection.url="jdbc:mysql://${DB_HOST}:3306/scrolls" \
    -Dhibernate.connection.username="${DB_USER}" \
    -Dhibernate.connection.password="${DB_PASS}" \
    -jar turtles-3.0-SNAPSHOT.jar