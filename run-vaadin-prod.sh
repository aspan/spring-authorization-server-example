#!/usr/bin/env bash

./start-authorization-server.sh
sleep 10
java -jar vaadin-web-application/target/vaadin-web-application-0.0.1-SNAPSHOT.jar
./stop-authorization-server.sh