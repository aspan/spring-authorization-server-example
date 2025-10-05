#!/usr/bin/env bash

./start-authorization-server.sh
./mvnw -pl :vaadin-web-application spring-boot:run
./stop-authorization-server.sh