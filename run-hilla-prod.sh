#!/usr/bin/env bash

./start-authorization-server.sh
sleep 5
java -jar hilla-web-application/target/hilla-web-application-0.0.1-SNAPSHOT.jar
./stop-authorization-server.sh