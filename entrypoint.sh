#!/usr/bin/env bash

echo "[client]
user=${DB_USER}
password=${DB_PASS}
host=${DB_HOST}" > ~/.my.cnf

chmod 600 ~/.my.cnf

if [[ ! -f "Server.jar" ]]
then
  if [[ ! -f "download/callersbane.zip" ]]
  then
    mkdir -p download
    curl -o download/callersbane.zip "https://download.scrolls.com/callersbane/server/CallersBane-Server-2.0.1.zip"
  fi

  cp download/callersbane.zip .
  unzip callersbane.zip

  cp -rf CallersBane-Server/* .
  rm -rf CallersBane-Server

  if [[ ! -f "Server.jar" ]]
  then
    echo "Failed to get Server.jar"
    exit 1
  fi
fi

while ! mysqladmin ping --silent
do
    echo "Waiting for database to be active"
    sleep 1
done

result=$(mysql -s -N -e "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='scrolls'")
if [ "$result" -eq "0" ]
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